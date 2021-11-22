package com.emmm.mobv.screens.transactions

data class TransactionItemDto(
    val subject: String,
    val amount: String,
    val assetName: String,
    val createdAt: String,
    val income: Boolean
)