package com.emmm.mobv.data.api

import android.content.Context
import okhttp3.OkHttpClient
import org.stellar.sdk.responses.AccountResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface WebApi {

    @GET("/accounts/{account}")
    suspend fun getAccountInfo(@Path("account") accountId: String): Response<AccountResponse>

    companion object {
        private const val TESTNET_URL = "https://horizon-testnet.stellar.org"

        fun create(context: Context): WebApi {

            val client = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(TESTNET_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WebApi::class.java)
        }
    }
}