package com.emmm.mobv.data

import android.util.Log
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
}