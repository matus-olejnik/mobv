package com.emmm.mobv.screens.transactions

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.databinding.TransactionItemBinding

class TransactionsViewHolder constructor(
    private val dataBinding: TransactionItemBinding,
    private val contactsViewModel: TransactionsViewModel
) : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(item: TransactionItem) {

        val subject = if (item.ownerAccountId == item.fromAccountId) item.toAccountId else item.fromAccountId
        val transactionItemDto = TransactionItemDto(
            subject,
            item.amount,
            item.assetName,
            item.createdAt,
            item.ownerAccountId == item.toAccountId
        )
        Log.i("TransactionsViewHolder", transactionItemDto.toString())
        dataBinding.transactionItemDto = transactionItemDto
    }
}