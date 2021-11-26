package com.emmm.mobv.screens.logout;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch

class LogoutViewModel(private val repository: DataRepository) : ViewModel() {

    fun logout(accountId: String) {
        viewModelScope.launch {
            repository.deleteUserData(accountId)
        }
    }
}