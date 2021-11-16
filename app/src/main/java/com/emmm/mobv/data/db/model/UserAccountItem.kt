package com.emmm.mobv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccountItem(
    @PrimaryKey val accountId: String,
    val secretSeedEncrypted: String,
    var moneyBalance: String? //TODO change to BigDecimal, now String because of converter needed
)