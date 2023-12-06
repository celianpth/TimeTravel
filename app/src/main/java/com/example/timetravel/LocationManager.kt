package com.example.timetravel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationManager(private val context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: ((Double, Double) -> Unit)? = null
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
    fun requestLocationUpdates(callback: (Double, Double) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = callback

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLastLocation()
            } else {
                requestLocationPermission()
            }
        } else {
            getLastLocation()
        }
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        onRequestPermissionsResult(
            LOCATION_PERMISSION_REQUEST_CODE,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            intArrayOf(PackageManager.PERMISSION_GRANTED)
        )
    }
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, fetch the last location
                    getLastLocation()
                } else {
                    // Permission denied, handle accordingly
                    // You may want to show a message to the user or disable location features
                }
            }
        }
    }
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                locationCallback?.invoke(latitude, longitude)
            }
        }
    }

}