package com.example.currencies.api.responses

import com.google.gson.annotations.SerializedName

data class BankResponse(
    @SerializedName("title") val title : String,
    @SerializedName("date") val date : Long,
    @SerializedName("logo") val logo : String,
    @SerializedName("list") val list : Map<String, Map<String, RateResponse>>
)

data class RateResponse(
    @SerializedName("buy") val buy : Double,
    @SerializedName("sell") val sell : Double
)