package com.emmm.mobv.screens.welcome;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.ContactItem
import kotlinx.coroutines.launch

class WelcomeViewModel(private val repository: DataRepository) : ViewModel() {
    val input: MutableLiveData<String> = MutableLiveData()

    fun insertContact() {
        viewModelScope.launch {
            repository.insertContact(ContactItem("tmpId", "nameTmp", "mainAccountIdTmp", "contactAccIdTmp"))
        }

    }

}