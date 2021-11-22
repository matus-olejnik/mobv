package com.emmm.mobv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_tbl")
data class TransactionItem(
    @PrimaryKey val id: String,
    val ownerAccountId: String,
    val fromAccountId: String,
    val toAccountId: String,
    val amount: String,
    val assetName: String,
    val createdAt: String,
)