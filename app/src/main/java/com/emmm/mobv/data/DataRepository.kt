package com.emmm.mobv.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.emmm.mobv.data.api.WebApi
import com.emmm.mobv.data.db.LocalCache
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.UserAccountItem
import com.emmm.mobv.util.CryptoUtil
import com.emmm.mobv.util.StellarUtil
import org.stellar.sdk.*
import org.stellar.sdk.responses.SubmitTransactionResponse

class DataRepository private constructor(
    private val api: WebApi,
    private val cache: LocalCache
) {

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

    suspend fun sendMoney(fromAccountId: String, toAccountId: String, amount: String, pinCode: String) {
        Log.i("DataRepository", "sending money from $fromAccountId to $toAccountId")

        val userAccountItem = getUserAccountItem(fromAccountId)

        val fromAccountSecret = CryptoUtil.decrypt(userAccountItem.secretSeedEncrypted, pinCode)!!

        val server = Server(StellarUtil.TESTNET_URL)
        val source = KeyPair.fromSecretSeed(fromAccountSecret)
        val destination = KeyPair.fromAccountId(toAccountId)

        server.accounts().account(source.accountId)
        server.accounts().account(destination.accountId)

        val sourceAccount = server.accounts().account(source.accountId)

        val transaction: Transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
            .addOperation(
                PaymentOperation.Builder(destination.accountId, AssetTypeNative(), amount).build()
            )
            .addMemo(Memo.text("Test Transaction"))
            .setTimeout(180)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build()
        transaction.sign(source)

        try {
            val response: SubmitTransactionResponse = server.submitTransaction(transaction)
            Log.i("DataRepository", "sending money successful\n$response")
        } catch (e: Exception) {
            Log.i("DataRepository", "error while sending money\n${e.message}")
        }
    }

    suspend fun getUserAccountItem(accountId: String): UserAccountItem {
        Log.i("DataRepository", "getting user account item for accountId=${accountId}")
        return cache.getUserAccountItem(accountId)
    }

    suspend fun deleteUserData(accountId: String) {
        cache.deleteUserData(accountId)
    }
}