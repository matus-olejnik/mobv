package com.emmm.mobv.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.TransactionItem
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

    @Query("SELECT * FROM contact WHERE mainAccountId = :mainAccountId")
    suspend fun getAllContacts2(mainAccountId: String): List<ContactItem>

    @Query("SELECT ua.accountId FROM user_account ua LIMIT 1")
    fun getActualUserAccountId(): String

    @Query("DELETE FROM user_account WHERE accountId = :accountId")
    suspend fun deleteUserData(accountId: String)

    @Query("SELECT * FROM transaction_tbl WHERE ownerAccountId = :accountId ORDER BY date(createdAt) DESC")
    fun getAllTransactions(accountId: String): LiveData<List<TransactionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionItem: TransactionItem)
}