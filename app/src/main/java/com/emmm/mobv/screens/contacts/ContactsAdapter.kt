package com.emmm.mobv.screens.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.databinding.ContactItemBinding

class ContactsAdapter(private val contactsViewModel: ContactsViewModel) : RecyclerView.Adapter<ContactsViewHolder>() {

    var contacts = listOf<ContactItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ContactItemBinding.inflate(inflater, parent, false)
        return ContactsViewHolder(dataBinding, contactsViewModel)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.setup(contacts[position])
    }
}