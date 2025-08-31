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

            var location = fusedLocationClient.lastLocation

            if (location.await() == null) {
                location = fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            }

            location.await()?.let {
                val latitudeRounded = "%.6f".format(it.latitude).toDouble()
                val longitudeRounded = "%.6f".format(it.longitude).toDouble()

                emit(
                    Location(
                        latitude = latitudeRounded,
                        longitude = longitudeRounded
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

}