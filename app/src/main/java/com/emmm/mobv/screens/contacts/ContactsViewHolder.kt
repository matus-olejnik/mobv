package com.emmm.mobv.screens.contacts

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.databinding.ContactItemBinding

class ContactsViewHolder constructor(
    private val dataBinding: ContactItemBinding,
    private val contactsViewModel: ContactsViewModel
) : RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(contactItem: ContactItem) {
        dataBinding.contactItem = contactItem

        itemView.setOnClickListener {
            itemView.findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToOrdersFragment2()
                    .setContactAccountId(contactItem.contactAccountId)
            )

        }
    }
}