package com.emmm.mobv.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.emmm.mobv.data.api.WebApi
import com.emmm.mobv.data.db.LocalCache
import com.emmm.mobv.data.db.SendMoneyResult
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.data.db.model.UserAccountItem
import com.emmm.mobv.util.CryptoUtil
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal

class DataRepository private constructor(
    private val api: WebApi,
    private val cache: LocalCache
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(api: WebApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(api, cache).also { INSTANCE = it }
            }
    }

    suspend fun createNewUserAccount(userAccountItem: UserAccountItem) {
        Log.i("DataRepository", "creating new user account $userAccountItem")
        cache.createNewUserAccount(userAccountItem)
    }

    suspend fun insertContacts(contactItems: List<ContactItem>) {
        cache.insertContacts(contactItems)
    }

    suspend fun insertContact(contactItem: ContactItem) {
        cache.insertContact(contactItem)
    }

    suspend fun insertTransaction(transactionItem: TransactionItem) {
        cache.insertTransaction(transactionItem)
    }

    suspend fun updateContact(contactItem: ContactItem) {
        cache.updateContact(contactItem)
    }

    suspend fun deleteContact(contactItem: ContactItem) {
        cache.deleteContact(contactItem)
    }

    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>> {
        Log.i("DataRepository", "getting all contacts")
        return cache.getAllContacts(mainAccountId)
    }

    suspend fun getAllContacts2(mainAccountId: String): List<ContactItem> {
        Log.i("DataRepository", "getting all contacts")
        return cache.getAllContacts2(mainAccountId)
    }

    fun getAllTransactions(mainAccountId: String): LiveData<List<TransactionItem>> {
        Log.i("DataRepository", "getting all transactions")
        return cache.getAllTransactions(mainAccountId)
    }

    //TODO other currencies
    suspend fun getActualBalance(accountId: String): String {
        Log.i("DataRepository", "checking balance for account $accountId")

        val response = api.getAccountInfo(accountId)

        val output: StringBuilder = StringBuilder()

        if (response.isSuccessful) {
            Log.i("DataRepository", "successfully fetched balance \n${response}")

            for (balance in response.body()!!.balances) {
                val actBalance = balance.balance

                output.append(actBalance)
                Log.i("DataRepository", actBalance)
            }
            val outputString = output.toString()

            val userAccountItem = cache.getUserAccountItem(accountId)
            userAccountItem.moneyBalance = outputString
            cache.updateUserAccountItem(userAccountItem)

            return outputString
        } else {
            Log.i("DataRepository", "error while fetching balance\n${response}")
        }

        return ""
    }

    suspend fun sendMoney(
        fromAccountId: String,
        toAccountId: String,
        amount: String,
        pinCode: String
    ): SendMoneyResult =
        withContext(ioDispatcher) {
            Log.i("DataRepository", "sending money from $fromAccountId to $toAccountId")

            val userAccountItem = getUserAccountItem(fromAccountId)

            val fromAccountSecret: String
            try {
                fromAccountSecret = CryptoUtil.decrypt(userAccountItem.secretSeedEncrypted, pinCode)!!

            } catch (e: Exception) {
                return@withContext SendMoneyResult.BAD_ACCOUNT_ID_OR_PIN
            }

            val server = Server(StellarUtil.TESTNET_URL)
            val source = KeyPair.fromSecretSeed(fromAccountSecret)
            val destination: KeyPair

            try {
                destination = KeyPair.fromAccountId(toAccountId)
                server.accounts().account(source.accountId)
                server.accounts().account(destination.accountId)
            } catch (e: Exception) {
                return@withContext SendMoneyResult.BAD_ACCOUNT_ID_OR_PIN
            }

            val sourceAccount: AccountResponse

            sourceAccount = server.accounts().account(source.accountId)

            val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
                .addOperation(
                    PaymentOperation.Builder(destination.accountId, AssetTypeNative(), amount).build()
                )
                .addMemo(Memo.text("Test Transaction"))
                .setTimeout(180)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build()
            transaction.sign(source)

            return@withContext try {
                val response: SubmitTransactionResponse = server.submitTransaction(transaction)
                Log.i("DataRepository", "sending money successful\n$response")
                SendMoneyResult.SUCCESSFUL
            } catch (e: Exception) {
                Log.i("DataRepository", "error while sending money\n${e.message}")
                SendMoneyResult.FAIL
            }
        }

    suspend fun fetchTransactions(accountId: String) = withContext(ioDispatcher) {
        Log.i("DataRepository", "fetching transactions")

        val output: StringBuilder = StringBuilder()

        val response = api.getAllPayments(accountId, null, null, 200)

        if (response.isSuccessful) {
            Log.i("DataRepository", "successfully fetched transactions\n\n$response")

            for (record in response.body()!!._embedded.records) {
                if (record.type == "payment") {
                    Log.i("DataRepository", "process transaction")

                    output.append(record.toString())
                    output.append("\n\n")

                    val transactionItem = TransactionItem(
                        record.transactionHash,
                        accountId,
                        record.from,
                        record.to,
                        record.amount,
                        record.assetType,
                        record.createdAt
                    )
                    insertTransaction(transactionItem)
                }
            }

        } else {
            Log.i("DataRepository", "error while fetching transactions\n${response.errorBody()}")
        }

        Log.i("DataRepository", "transactions for $accountId\n$output")
    }

    suspend fun getUserAccountItem(accountId: String): UserAccountItem {
        Log.i("DataRepository", "getting user account item for accountId=${accountId}")
        return cache.getUserAccountItem(accountId)
    }

    suspend fun deleteUserData(accountId: String) {
        cache.deleteUserData(accountId)
    }

    suspend fun calculateUsdBalance(xlmBalance: BigDecimal): BigDecimal? {
        Log.i("DataRepository", "fetching external proces")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.stellarterm.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val response = service.getExternalPrices()

        if (response.isSuccessful) {
            Log.i("DataRepository", "successfully fetched external proces \n$response\n${response.body()}")

            val rate = BigDecimal(response.body()?._meta?.externalPrices?.USD_XLM)
            return rate.multiply(xlmBalance).setScale(2, BigDecimal.ROUND_HALF_UP)
        } else {
            Log.i("StellarUtil", "error while fetching external proces\n${response}")
        }
        return null
    }
}