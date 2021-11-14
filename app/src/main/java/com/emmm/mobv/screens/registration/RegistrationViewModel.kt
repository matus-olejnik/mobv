package com.emmm.mobv.screens.registration;

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    val secretSeedTextView: MutableLiveData<String> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    fun registerNewUser() {
        viewModelScope.launch {
            progressBarVisibility.value = View.VISIBLE

            val secretSeed = StellarUtil.createAccount()
            secretSeedTextView.postValue(secretSeed)

            progressBarVisibility.value = View.GONE
        }
    }
}