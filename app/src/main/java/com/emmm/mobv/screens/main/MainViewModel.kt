package com.emmm.mobv.screens.main;

import androidx.lifecycle.*
import com.emmm.mobv.data.DataRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    val currentUserID: MutableLiveData<String> = MutableLiveData("")

    val moneyBalanceTextView: MutableLiveData<String> = MutableLiveData("")
    private val usdBalance: MutableLiveData<BigDecimal> = MutableLiveData()
    val tranUsdBalance: LiveData<String> = Transformations.map(usdBalance) { "$ ${it ?: ""}" }

    private val balanceChekCount: MutableLiveData<Int> = MutableLiveData(0)

    fun fetchActualBalance(accountId: String) {
        viewModelScope.launch {
            val actualBalance: String = if (balanceChekCount.value!! % 3 == 0) {
                repository.getActualBalance(accountId)
            } else {
                repository.getActualBalanceFromDb(accountId)
            }

            val xml = " XLM"
            moneyBalanceTextView.value = String.format("%.2f", actualBalance.toFloat()) + xml
            usdBalance.value = repository.calculateUsdBalance(BigDecimal(actualBalance))
            val tmpCount = balanceChekCount.value
            balanceChekCount.value = tmpCount?.plus(1)
        }
    }

    fun updateCurrentUserId(accountId: String) {
        currentUserID.value = accountId
    }
}