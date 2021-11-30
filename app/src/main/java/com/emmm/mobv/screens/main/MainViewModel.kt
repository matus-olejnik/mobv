package com.emmm.mobv.screens.main;

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val moneyBalanceTextView: MutableLiveData<String> = MutableLiveData("")
    val exchangeToEuroTextView: MutableLiveData<String> = MutableLiveData("")
    val exchangeToEuroBalance: MutableLiveData<String> = MutableLiveData("€")

    val tmpTextView: MutableLiveData<String> = MutableLiveData()

    fun fetchActualBalance(accountId: String) {
        viewModelScope.launch {
            val actualBalance = repository.getActualBalance(accountId)
            val xml = " XLM"
            moneyBalanceTextView.value = String.format("%.2f", actualBalance.toFloat()) + xml
        }
    }

    fun fetchCurrentUser(accountId: String) {
        viewModelScope.launch {
            tmpTextView.value = repository.getUserAccountItem(accountId).toString()
        }
    }

    fun exchangeXMLtoEUR(accountId: String) {
        viewModelScope.launch {
            val actualBalance = repository.getActualBalance(accountId)
            try {
                val exchangedValue = (actualBalance.toFloat() * 0.303)
                exchangeToEuroTextView.value = String.format("%.2f", exchangedValue)
                exchangeToEuroBalance.value = "€ " + String.format("%.2f", exchangedValue)
            }
            catch (nfe: NumberFormatException){
                Log.i("Exchange XLM: ", "Wrong number format")
            }
        }
    }
}