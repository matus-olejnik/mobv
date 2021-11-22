package com.emmm.mobv.screens.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.data.db.model.TransactionItem
import com.emmm.mobv.databinding.TransactionItemBinding

class TransactionsAdapter(private val contactsViewModel: TransactionsViewModel) :
    RecyclerView.Adapter<TransactionsViewHolder>() {

    var transactions = listOf<TransactionItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = TransactionItemBinding.inflate(inflater, parent, false)
        return TransactionsViewHolder(dataBinding, contactsViewModel)
    }

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.setup(transactions[position])
    }
}