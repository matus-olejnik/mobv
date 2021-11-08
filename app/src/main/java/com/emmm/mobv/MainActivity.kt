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
                viewModel.checkBalance("GD3PFUAY2SOPJUFR6UGR6PB3UJYRVMWWMEVN46KOX7GDLLBZJLLDYCQM")
            }

            sendMoneyButton.setOnClickListener {
                viewModel.sendMoney(
                    "SCCJH5NTT7XX7E6Q4DB2VRPZI7JRNEJMSWAC2VMFHD436NZLDJF2OKDR",
                    "GA2HZILTHPO334QK4GIN66TRXM4TACVPWSLFOEQVYP4CGW2URKDSRKWZ",
                    "1000"
                )
            }

            showTransactionsButton.setOnClickListener {
                viewModel.showTransactions("GD3PFUAY2SOPJUFR6UGR6PB3UJYRVMWWMEVN46KOX7GDLLBZJLLDYCQM")
            }
        }
    }
}