package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.data.db.model.UserAccountItem

class LocalCache(private val dbDao: DbDao) {

    suspend fun createNewUserAccount(userAccountItem: UserAccountItem) {
        dbDao.createNewUserAccount(userAccountItem)
    }

    suspend fun getUserAccountItem(accountId: String): UserAccountItem {
        return dbDao.getUserAccountItem(accountId)
    }

    suspend fun updateUserAccountItem(userAccountItem: UserAccountItem) {
        dbDao.updateUserAccountItem(userAccountItem)
    }

    suspend fun insertContacts(contactItems: List<ContactItem>) {
        dbDao.insertContacts(contactItems)
    }

    suspend fun insertContact(contactItem: ContactItem) {
        dbDao.insertContact(contactItem)
    }

    suspend fun updateContact(contactItem: ContactItem) {
        dbDao.updateContact(contactItem)
    }

    suspend fun deleteContact(contactItem: ContactItem) {
        dbDao.deleteContact(contactItem)
    }

    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>> {
        return dbDao.getAllContacts(mainAccountId)
    }

    suspend fun getAllContacts2(mainAccountId: String): List<ContactItem> {
        return dbDao.getAllContacts2(mainAccountId)
    }

    fun getActualUserAccountId(): String {
        return dbDao.getActualUserAccountId()
    }

    suspend fun deleteUserData(accountId: String) {
        dbDao.deleteUserData(accountId)
    }

    fun getAllTransactions(mainAccountId: String): LiveData<List<TransactionItem>> {
        return dbDao.getAllTransactions(mainAccountId)
    }

    suspend fun insertTransaction(transactionItem: TransactionItem) {
        dbDao.insertTransaction(transactionItem)
    }

    suspend fun getActualBalanceFromDb(accountId: String): String {
        return dbDao.getActualBalanceFromDb(accountId)
    }
}