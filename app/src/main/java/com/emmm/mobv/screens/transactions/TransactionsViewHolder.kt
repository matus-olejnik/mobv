package com.emmm.mobv.screens.transactions

import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.databinding.TransactionItemBinding
import com.emmm.mobv.util.DateUtil

class TransactionsViewHolder constructor(
    private val dataBinding: TransactionItemBinding,
    private val contactsViewModel: TransactionsViewModel
) : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(item: TransactionItem) {

        val subject = if (item.ownerAccountId == item.fromAccountId) item.toAccountId else item.fromAccountId
        val transactionItemDto = TransactionItemDto(
            subject,
            String.format("%.2f", item.amount.toFloat()),
            if (item.assetName == "native") "lumens" else item.assetName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) DateUtil.formatDate(item.createdAt) else item.createdAt,
            item.ownerAccountId == item.toAccountId
        )
        Log.i("TransactionsViewHolder", transactionItemDto.toString())
        dataBinding.transactionItemDto = transactionItemDto
    }
}