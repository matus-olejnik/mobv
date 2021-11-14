package com.emmm.mobv.screens.registration;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    companion object {
        val REGISTRATION_STARTED = 0
        val REGISTRATION_FINISHED = 1
    }

    var registrationPhase: MutableLiveData<Int> = MutableLiveData()

    val secretSeedTextView: MutableLiveData<String> = MutableLiveData()

    fun registerNewUser() {
        viewModelScope.launch {
            registrationPhase.value = REGISTRATION_STARTED

            val secretSeed = StellarUtil.createAccount()
            secretSeedTextView.postValue(secretSeed)

            registrationPhase.value = REGISTRATION_FINISHED
        }
    }
}