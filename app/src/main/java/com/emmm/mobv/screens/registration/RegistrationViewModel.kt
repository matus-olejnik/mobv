package com.emmm.mobv.screens.registration;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    val secretSeedTextView: MutableLiveData<String> = MutableLiveData()

    fun registerNewUser() {
        viewModelScope.launch {
            val secretSeed = StellarUtil.createAccount()
            secretSeedTextView.postValue(secretSeed)
        }
    }
}