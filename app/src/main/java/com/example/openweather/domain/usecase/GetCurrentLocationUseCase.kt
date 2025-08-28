package com.example.openweather.domain.usecase

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.openweather.presentation.models.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

interface GetCurrentLocationUseCase {
    operator fun invoke(): Flow<Location>
}

class GetCurrentLocationUseCaseImpl(
    private val context: Context
): GetCurrentLocationUseCase {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun invoke(): Flow<Location> = flow {
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            // Step 1: Try last known location
            var location = fusedLocationClient.lastLocation.await()

            // Step 2: If last location is null, request a fresh one
            if (location == null) {
                location = fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .await()
            }

            // Step 3: Update StateFlow
            location?.let {
                emit(
                    Location(
                        latitude = location.latitude.toInt().toString(),
                        longitude = location.longitude.toInt().toString()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

}