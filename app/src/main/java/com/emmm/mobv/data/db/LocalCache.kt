package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import com.emmm.mobv.data.db.model.ContactItem

class LocalCache(private val dbDao: DbDao) {

    fun insertContacts(contactItems: List<ContactItem>) {
        dbDao.insertContacts(contactItems)
    }

    fun insertContact(contactItem: ContactItem) {
        dbDao.insertContact(contactItem)
    }

    fun updateContact(contactItem: ContactItem) {
        dbDao.updateContact(contactItem)
    }

    fun deleteContact(contactItem: ContactItem) {
        dbDao.deleteContact(contactItem)
    }

    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>> {
        return dbDao.getAllContacts(mainAccountId)
    }
}