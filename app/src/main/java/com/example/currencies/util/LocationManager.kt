package com.example.currencies.util

import android.location.Location

//TODO for simplicity and because of limited time, let's have a fake location
object LocationManager {

    fun getCurrentLocation() = Location("").apply {
        this.latitude = 40.208218
        this.longitude = 44.536690
    }

}