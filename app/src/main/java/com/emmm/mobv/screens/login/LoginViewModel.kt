package com.emmm.mobv.screens.login;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.UserAccountItem
import com.emmm.mobv.util.CryptoUtil
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    val secretSeedEditText: MutableLiveData<String> = MutableLiveData()

    val pinEditText: MutableLiveData<String> = MutableLiveData()

    fun loginNewUser() {
        viewModelScope.launch {
            val accountId = StellarUtil.getAccountIdFromSecretSeed(secretSeedEditText.value!!)

            val encryptedSecret = CryptoUtil.encrypt(secretSeedEditText.value!!, pinEditText.value!!)

            val userAccountItem = UserAccountItem(
                UUID.randomUUID().toString(),
                accountId,
                encryptedSecret!!,
                null
            )

            repository.createNewUserAccount(userAccountItem)
        }
    }
}