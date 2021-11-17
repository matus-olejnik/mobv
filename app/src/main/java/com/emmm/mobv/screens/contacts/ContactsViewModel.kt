package com.emmm.mobv.screens.contacts;

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.ContactItem
import kotlinx.coroutines.launch
import java.util.*

class ContactsViewModel(private val repository: DataRepository) : ViewModel() {

    val contacts: MutableLiveData<List<ContactItem>> = MutableLiveData()

    val contactNameEditText: MutableLiveData<String> = MutableLiveData()

    val contactAccountIdEditText: MutableLiveData<String> = MutableLiveData()

    fun insertContact(accountId: String) {
        viewModelScope.launch {
            val contactItem = ContactItem(
                UUID.randomUUID().toString(),
                contactNameEditText.value!!,
                accountId,
                contactAccountIdEditText.value!!
            )
            Log.i("ContactsViewModel", "inserting $contactItem")

            repository.insertContact(contactItem)
        }
    }

    fun fetchContacts(accountId: String) {
        viewModelScope.launch {
            contacts.value = repository.getAllContacts(accountId)
        }
    }
}