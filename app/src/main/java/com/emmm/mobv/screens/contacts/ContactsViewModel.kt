package com.emmm.mobv.screens.contacts;

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.ContactItem
import kotlinx.coroutines.launch
import java.util.*

class ContactsViewModel(private val repository: DataRepository) : ViewModel() {

    private val mainAccountId = "testAccountId" //TODO change "testAccountId" dynamically

    val contacts: LiveData<List<ContactItem>>
        get() = repository.getAllContacts(mainAccountId)

    val contactNameEditText: MutableLiveData<String> = MutableLiveData()

    val contactAccountIdEditText: MutableLiveData<String> = MutableLiveData()

    fun insertContact() {
        viewModelScope.launch {
            val contactItem = ContactItem(
                UUID.randomUUID().toString(),
                contactNameEditText.value ?: "Nezadane meno",
                mainAccountId,
                contactAccountIdEditText.value ?: "Nezadane account id"
            )
            Log.i("ContactsViewModel", "inserting $contactItem")

            repository.insertContact(contactItem)
        }

    }
}