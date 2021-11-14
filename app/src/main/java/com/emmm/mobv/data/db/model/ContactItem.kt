package com.emmm.mobv.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class ContactItem(
    @PrimaryKey val id: String,
    val name: String,
    val mainAccountId: String,
    val contactAccountId: String
)