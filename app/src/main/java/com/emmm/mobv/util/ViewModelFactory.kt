package com.emmm.mobv.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.screens.contacts.ContactsViewModel
import com.emmm.mobv.screens.main.MainViewModel

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }

        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}