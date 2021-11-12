package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emmm.mobv.data.db.model.ContactItem

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contactItems: List<ContactItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contactItem: ContactItem)

    @Update
    fun updateContact(contactItem: ContactItem)

    @Delete
    fun deleteContact(contactItem: ContactItem)

    @Query("SELECT * FROM contact WHERE mainAccountId = :mainAccountId")
    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>>
}