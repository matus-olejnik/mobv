package com.emmm.mobv.data

import androidx.lifecycle.LiveData
import com.emmm.mobv.data.db.LocalCache
import com.emmm.mobv.data.db.model.ContactItem

class DataRepository private constructor(
    private val cache: LocalCache
) {

    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(cache).also { INSTANCE = it }
            }
    }

    fun insertContacts(contactItems: List<ContactItem>) {
        cache.insertContacts(contactItems)
    }

    fun insertContact(contactItem: ContactItem) {
        cache.insertContact(contactItem)
    }

    fun updateContact(contactItem: ContactItem) {
        cache.updateContact(contactItem)
    }

    fun deleteContact(contactItem: ContactItem) {
        cache.deleteContact(contactItem)
    }

    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>> {
        return cache.getAllContacts(mainAccountId)
    }
}