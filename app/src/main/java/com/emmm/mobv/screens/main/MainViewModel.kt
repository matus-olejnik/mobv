package com.emmm.mobv.screens.main;

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.PriceRequest
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val currentUserID: MutableLiveData<String> = MutableLiveData("")
    val moneyBalanceTextView: MutableLiveData<String> = MutableLiveData("")
    val exchangeToEuroTextView: MutableLiveData<String> = MutableLiveData("")
    val exchangeToEuroBalance: MutableLiveData<String> = MutableLiveData("€")

    val tmpTextView: MutableLiveData<String> = MutableLiveData()

    fun fetchActualBalance(accountId: String) {
        viewModelScope.launch {
            val actualBalance = repository.getActualBalance(accountId)
            val xml = " XLM"
            moneyBalanceTextView.value = String.format("%.2f", actualBalance.toFloat()) + xml

            //Exchange
            try {
                val actualPrice = PriceRequest().getActualRate()
                Log.d("Debug request", "$actualPrice")
                val exchangedValue = (actualBalance.toFloat() * actualPrice.toFloat())
                exchangeToEuroTextView.value = String.format("%.2f", exchangedValue)
                exchangeToEuroBalance.value = "€ " + String.format("%.2f", exchangedValue)
            } catch (nfe: NumberFormatException) {
                Log.i("Exchange XLM", "Wrong number format")
            }
        }
    }

    fun fetchCurrentUserID(accountId: String) {
        viewModelScope.launch {
            currentUserID.value = repository.getUserAccountItem(accountId).accountId
        }
    }

    fun fetchCurrentUser(accountId: String) {
        viewModelScope.launch {
            tmpTextView.value = repository.getUserAccountItem(accountId).toString()
        }
    }
}