package com.emmm.mobv.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.data.DataRepository
import com.emmm.mobv.screens.login.LoginViewModel
import com.emmm.mobv.screens.main.MainViewModel
import com.emmm.mobv.screens.orders.OrdersViewModel
import com.emmm.mobv.screens.registration.RegistrationViewModel
import com.emmm.mobv.screens.welcome.WelcomeViewModel

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel() as T
        }

        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistrationViewModel() as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrdersViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}