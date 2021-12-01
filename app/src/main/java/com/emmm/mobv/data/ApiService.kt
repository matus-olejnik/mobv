package com.emmm.mobv.data

import com.emmm.mobv.data.api.model.TickerResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Used for friend bot url https://friendbot.stellar.org
     */
    @GET("/")
    suspend fun callFriendBot(@Query("addr") addr: String): Response<ResponseBody>

    @GET("/accounts/{account}")
    suspend fun getAccountInfo(@Path("account") accountId: String): Response<ResponseBody>

    @GET("/v1/ticker.json")
    suspend fun getExternalPrices(): Response<TickerResponse>
}