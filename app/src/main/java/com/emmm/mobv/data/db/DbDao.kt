package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.UserAccountItem

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewUserAccount(userAccountItem: UserAccountItem)

    @Query("SELECT * FROM user_account WHERE accountId = :accountId")
    suspend fun getUserAccountItem(accountId: String): UserAccountItem

    @Update
    suspend fun updateUserAccountItem(userAccountItem: UserAccountItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contactItems: List<ContactItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactItem: ContactItem)

    @Update
    suspend fun updateContact(contactItem: ContactItem)

    @Delete
    suspend fun deleteContact(contactItem: ContactItem)

    @Query("SELECT * FROM contact WHERE mainAccountId = :mainAccountId")
    fun getAllContacts(mainAccountId: String): LiveData<List<ContactItem>>

    @Query("SELECT ua.accountId FROM user_account ua LIMIT 1")
    fun getActualUserAccountId(): String
}