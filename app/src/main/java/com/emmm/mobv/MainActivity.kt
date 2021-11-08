package com.emmm.mobv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.apply {
            createAccountButton.setOnClickListener {
                viewModel.createAccount()
            }

            balanceButton.setOnClickListener {
                viewModel.checkBalance("GDXDVP3JB67JKXVTHKONSELWF5WRSZK2QSXQYNBSLYCJCCDK37I2WLWK")
            }

            sendMoneyButton.setOnClickListener {
                viewModel.sendMoney()
            }
        }
    }
}