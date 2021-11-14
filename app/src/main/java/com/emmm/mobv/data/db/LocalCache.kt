package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.UserAccountItem

class LocalCache(private val dbDao: DbDao) {

    suspend fun createNewUserAccount(userAccountItem: UserAccountItem) {
        dbDao.createNewUserAccount(userAccountItem)
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
}