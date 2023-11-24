package com.abc.daily.util

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import javax.inject.Inject

class LocationHelper: LocationListener {

    @Inject
    lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (Location?) -> Unit) {

            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


    }

    override fun onLocationChanged(location: Location) {

    }

}