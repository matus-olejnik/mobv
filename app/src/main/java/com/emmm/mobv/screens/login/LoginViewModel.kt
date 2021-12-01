package com.emmm.mobv.screens.login;

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.data.db.model.UserAccountItem
import com.emmm.mobv.util.CryptoUtil
import com.emmm.mobv.util.StellarUtil
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    val secretSeedEditText: MutableLiveData<String> = MutableLiveData()

    val pinEditText: MutableLiveData<String> = MutableLiveData()

    var action: MutableLiveData<Action> = MutableLiveData()
    var accountId: MutableLiveData<String> = MutableLiveData()

    val wrongPublicKeyVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    fun loginNewUser() {
        viewModelScope.launch {

            try {

                val actAccountId =
                    StellarUtil.getAccountIdFromSecretSeed(secretSeedEditText.value!!)

                val encryptedSecret =
                    CryptoUtil.encrypt(secretSeedEditText.value!!, pinEditText.value!!)

                wrongPublicKeyVisibility.value = View.GONE

                val userAccountItem = UserAccountItem(
                    actAccountId,
                    encryptedSecret!!,
                    null
                )

                repository.createNewUserAccount(userAccountItem)

                accountId.value = actAccountId
                action.value = Action(Action.SHOW_MAIN_ACTIVITY)

            } catch (e: Exception) {
                when (e) {
                    is IllegalArgumentException,
                    is NullPointerException -> {
                        wrongPublicKeyVisibility.value = View.VISIBLE
                        secretSeedEditText.value = ""
                        pinEditText.value = ""
                    }
                }
            }

        }
    }

    //TODO good approach but change later, maybe just some String or Int
    class Action(val action: Int) {
        companion object {
            val SHOW_MAIN_ACTIVITY = 0
        }
    }
}