package com.emmm.mobv.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.emmm.mobv.data.api.WebApi
import com.emmm.mobv.data.db.LocalCache
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.data.db.model.UserAccountItem
import com.emmm.mobv.util.CryptoUtil
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.*
import org.stellar.sdk.requests.PaymentsRequestBuilder
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse

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

    fun getAllTransactions(mainAccountId: String): LiveData<List<TransactionItem>> {
        Log.i("DataRepository", "getting all transactions")
        return cache.getAllTransactions(mainAccountId)
    }

    suspend fun getActualBalance(accountId: String): String {
        Log.i("DataRepository", "checking balance for account $accountId")

        val response = api.getAccountInfo(accountId)

        val output: StringBuilder = StringBuilder()

        if (response.isSuccessful) {
            Log.i("DataRepository", "successfully fetched balance \n${response}")

            for (balance in response.body()!!.balances) {
                val actBalance = String.format(
                    "Balance: %s, Type: %s, Code: %s %n",
                    balance.balance,
                    balance.assetType,
                    balance.assetCode
                )

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

    suspend fun sendMoney(fromAccountId: String, toAccountId: String, amount: String, pinCode: String): Boolean =
        withContext(ioDispatcher) {
            Log.i("DataRepository", "sending money from $fromAccountId to $toAccountId")

            val userAccountItem = getUserAccountItem(fromAccountId)

            val fromAccountSecret = CryptoUtil.decrypt(userAccountItem.secretSeedEncrypted, pinCode)!!

            val server = Server(StellarUtil.TESTNET_URL)
            val source = KeyPair.fromSecretSeed(fromAccountSecret)
            val destination = KeyPair.fromAccountId(toAccountId)


            server.accounts().account(source.accountId)
            server.accounts().account(destination.accountId)

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
                true
            } catch (e: Exception) {
                Log.i("DataRepository", "error while sending money\n${e.message}")
                false
            }
        }

    suspend fun fetchTransactions(accountId: String) = withContext(ioDispatcher) {
        val output: StringBuilder = StringBuilder()

        val server = Server(StellarUtil.TESTNET_URL)
        val account = KeyPair.fromAccountId(accountId)
        val paymentRequestBuilder: PaymentsRequestBuilder = server.payments().forAccount(account.accountId)
        val operations: ArrayList<OperationResponse> = paymentRequestBuilder.execute().records

        for (payment in operations) {
            if (payment is PaymentOperationResponse) {
                val amount: String = payment.amount
                val asset: Asset = payment.asset
                val assetName: String = if (asset == AssetTypeNative()) {
                    "lumens"
                } else {
                    val assetNameBuilder: StringBuilder = StringBuilder()
                    assetNameBuilder.append(((asset as AssetTypeCreditAlphaNum).code))
                    assetNameBuilder.append(":")
                    assetNameBuilder.append(asset.issuer)
                    assetNameBuilder.toString()
                }
                output.append(amount)
                output.append(" ")
                output.append(assetName)
                output.append("\n hash ")
                output.append(payment.transactionHash)
                output.append("\n from ")
                output.append(payment.from)
                output.append("\n to ")
                output.append(payment.to)
                output.append("\n\n   ")

                val transactionItem = TransactionItem(
                    payment.transactionHash,
                    accountId,
                    payment.from,
                    payment.to,
                    payment.amount,
                    assetName,
                    payment.createdAt
                )
                insertTransaction(transactionItem)

            } else if (payment is CreateAccountOperationResponse) {
                output.append("Funder ")
                output.append(payment.funder)
                output.append("\nStarting balance ")
                output.append(payment.startingBalance)
            }
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
}