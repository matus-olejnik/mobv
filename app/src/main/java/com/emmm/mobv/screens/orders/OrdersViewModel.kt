package com.emmm.mobv.screens.orders;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: DataRepository) : ViewModel() {
    val tmp2TextView: MutableLiveData<String> = MutableLiveData()
    val confirmPinEditText: MutableLiveData<String> = MutableLiveData()
    val amountEditText: MutableLiveData<String> = MutableLiveData()

    private val _eventSendMoney = MutableLiveData<Boolean>()
    val eventSendMoney: LiveData<Boolean>
        get() = _eventSendMoney

    fun onSendMoney() {
        _eventSendMoney.value = true
    }

    fun sendMoney(contactAccountId: String) {
        viewModelScope.launch {
            repository.sendMoney()
        }
    }
}