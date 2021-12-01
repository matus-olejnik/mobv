package com.emmm.mobv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

        // Create SharedPreference
        val sharedPref: SharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val publicKey = sharedPref.getString("public_key", "")
        // Check if publicKey exist in SharedPreference
        if(!publicKey.equals("")){
            Log.i("SharedPref", "$publicKey")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("accountId", publicKey)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}