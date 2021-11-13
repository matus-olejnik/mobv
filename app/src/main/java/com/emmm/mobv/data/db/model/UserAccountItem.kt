package com.emmm.mobv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "user_account")
data class UserAccountItem(
    @PrimaryKey val id: String,
    val name: String,
    val accountId: String,
    val secretSeedEncrypted: String,
    val moneyBalance: BigDecimal,
)