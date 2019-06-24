package com.example.currencies.api.responses

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class BranchResponse(
    @SerializedName("address") val address: Map<String, String>,
    @SerializedName("contacts") val contacts: String,
    @SerializedName("head") val head: Long,
    @SerializedName("location") val location: LocationResponse,
    @SerializedName("title") val title: Map<String, String>,
    @SerializedName("workhours") val workHours: List<WorkHourResponse>
)

data class WorkHourResponse(
    @SerializedName("days") val days: String,
    @SerializedName("hours") val hours: String
)

data class BankDetailsResponse(
    @SerializedName("date") val date : Double,
    @SerializedName("list") val list : Map<String, BranchResponse>
)