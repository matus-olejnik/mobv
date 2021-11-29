package com.emmm.mobv.screens.contacts;

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.ContactItem
import kotlinx.coroutines.launch
import java.util.*

class ContactsViewModel(
    private val repository: DataRepository,
    private val accountId: String
) : ViewModel() {

    val contacts: LiveData<List<ContactItem>>
        get() = repository.getAllContacts(accountId)

    val contactNameEditText: MutableLiveData<String> = MutableLiveData()

    val contactAccountIdEditText: MutableLiveData<String> = MutableLiveData()

    fun insertContact(accountId: String) {
        if (contactNameEditText.value?.toString()?.equals("") == false &&
            contactAccountIdEditText.value?.toString()?.equals("") == false
        ) {
            viewModelScope.launch {
                val contactItem = ContactItem(
                    UUID.randomUUID().toString(),
                    contactNameEditText.value!!,
                    accountId,
                    contactAccountIdEditText.value!!
                )
                Log.i("ContactsViewModel", "inserting $contactItem")

                repository.insertContact(contactItem)
                contactNameEditText.value = ""
                contactAccountIdEditText.value = ""
            }
        }
    }
}