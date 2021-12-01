package com.emmm.mobv.screens.orders;

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.SendMoneyResult
import com.emmm.mobv.data.db.model.ContactItem
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: DataRepository) : ViewModel() {
    val confirmPinEditText: MutableLiveData<String> = MutableLiveData()
    val amountEditText: MutableLiveData<String> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)


    private var _contactsList: MutableLiveData<List<ContactItem>> = MutableLiveData()
    val contactsList: MutableLiveData<List<ContactItem>>
        get() = _contactsList

    private val _eventSendMoney = MutableLiveData<Boolean>()
    val eventSendMoney: LiveData<Boolean>
        get() = _eventSendMoney

    private val _eventMoneySent = MutableLiveData<SendMoneyResult>()
    val eventMoneySent: LiveData<SendMoneyResult>
        get() = _eventMoneySent

    fun onSendMoney() {
        _eventSendMoney.value = true
    }

    fun onSendMoneyDone() {
        _eventSendMoney.value = false
    }

    fun fetchAllContacts(accountId: String) {
        viewModelScope.launch {
            _contactsList.value = repository.getAllContacts2(accountId)
        }
    }

    fun sendMoney(fromAccountId: String, toAccountId: String) {
        viewModelScope.launch {
            _eventMoneySent.value =
                repository.sendMoney(
                    fromAccountId,
                    toAccountId,
                    amountEditText.value!!,
                    confirmPinEditText.value!!
                )
        }
        amountEditText.value = ""
        confirmPinEditText.value = ""
    }
}