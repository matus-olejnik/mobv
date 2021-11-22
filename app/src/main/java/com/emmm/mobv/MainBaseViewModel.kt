package com.emmm.mobv;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainBaseViewModel : ViewModel() {

    val actualAccountId: MutableLiveData<String> = MutableLiveData()
}