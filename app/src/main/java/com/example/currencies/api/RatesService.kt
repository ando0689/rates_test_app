package com.example.currencies.api

import androidx.lifecycle.LiveData
import com.example.currencies.api.responses.BankDetailsResponse
import com.example.currencies.api.responses.BankResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {

    //TODO language string should be constant
    @GET("rates.ashx")
    fun getRates(@Query("lang") lang: String = "en"): LiveData<ApiResponse<Map<String, BankResponse>>>

    @GET("branches.ashx")
    fun getBankDetails(@Query("id") id: String): LiveData<ApiResponse<BankDetailsResponse>>
}
