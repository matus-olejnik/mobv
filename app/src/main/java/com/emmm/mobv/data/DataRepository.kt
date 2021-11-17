package com.emmm.mobv.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.emmm.mobv.data.api.WebApi
import com.emmm.mobv.data.db.LocalCache
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.UserAccountItem

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

    suspend fun getUserAccountItem(accountId: String): UserAccountItem {
        return cache.getUserAccountItem(accountId)
    }
}