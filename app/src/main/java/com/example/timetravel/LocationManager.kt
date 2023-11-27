package com.example.timetravel

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationManager(private val context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun requestLocationUpdates(callback: (Double, Double) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLastLocation(callback)
            } else {
                // Demander la permission si elle n'a pas été accordée
                // (vous devrez gérer la réponse de l'utilisateur)
            }
        } else {
            getLastLocation(callback)
        }
    }

    private fun getLastLocation(callback: (Double, Double) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                callback(latitude, longitude)
            }
        }
    }
}