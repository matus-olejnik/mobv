package com.emmm.mobv.data

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class PriceRequest {
    companion object{
        var actualRate = ""
    }

    private fun run(){
        val apiUrl = "https://api.stellarterm.com/v1/ticker.json"
        val request = Request.Builder().url(apiUrl).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                val json_object = JSONObject(responseText)
                val json_array = json_object.getJSONObject("_meta")
                val pricesArray = json_array.getJSONObject("externalPrices")
                val stellarPrice = pricesArray.get("USD_XLM").toString()
                actualRate = stellarPrice
                //Log.i("Response", "$stellarPrice")
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("Response", "FAILED")
            }
        })
    }

    suspend fun getActualRate(): String {
        run()
        delay(500)
        return actualRate
    }
}