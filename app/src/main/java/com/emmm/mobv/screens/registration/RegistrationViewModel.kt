package com.emmm.mobv.screens.registration;

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    val secretSeedTextView: MutableLiveData<String> = MutableLiveData()
    val secretSeedTextViewVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val progressBarVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val checkboxChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkboxVisibility: MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    val registrationButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    val goToLoginPageButtonVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val errorTextViewVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    fun registerNewUser() {
        if (checkboxChecked.value == true) {
            viewModelScope.launch {
                progressBarVisibility.value = View.VISIBLE

                val secretSeed = StellarUtil.createAccount()
                secretSeedTextViewVisibility.value = View.VISIBLE
                secretSeedTextView.postValue(secretSeed)

                progressBarVisibility.value = View.GONE
                checkboxVisibility.value = View.GONE

                registrationButtonVisibility.value = View.GONE
                goToLoginPageButtonVisibility.value = View.VISIBLE
            }
        }
    }
}