package com.example.openweather.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.openweather.common.Resource
import com.example.openweather.data.database.dao.WeatherDao
import com.example.openweather.data.database.dao.WeatherWithForecastDao
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.database.entities.WeatherWithForecast
import com.example.openweather.data.remote.OpenWeatherApiService
import com.example.openweather.domain.mappers.onePerDay
import com.example.openweather.domain.mappers.toForeCastEntity
import com.example.openweather.domain.mappers.toForeCastUiModel
import com.example.openweather.domain.mappers.toWeatherEntity
import com.example.openweather.domain.mappers.toWeatherUiModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class OpenWeatherRepositoryImpl(
    private val openWeatherApiService: OpenWeatherApiService,
    private val weatherWithForecastDao: WeatherWithForecastDao,
    private val weatherDao: WeatherDao,
    private val context: Context
) : OpenWeatherRepository {
    override suspend fun insertWeather(weatherEntity: WeatherEntity) {
        withContext(Dispatchers.IO) {
            weatherWithForecastDao.insertWeather(weatherEntity)
        }
    }

    override suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): Resource<ImmutableList<ForecastUiModel>> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Loading<WeatherUiModel>()

                val fiveDayForecast = openWeatherApiService.getFiveDayWeatherForecast(
                    latitude = latitude,
                    longitude = longitude
                ).list
                .onePerDay()
                .map { it.toForeCastUiModel() }
                .toPersistentList()

                Resource.Success(data = fiveDayForecast)
            } catch (exception: HttpException) {
                Resource.Error(exception.localizedMessage ?: "An unexpected error occurred")
            } catch (exception: IOException) {
                Resource.Error("Could not reach server. Check your internet connection ${exception.localizedMessage}")
            }
        }

    }

    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Resource<WeatherUiModel> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Loading<WeatherUiModel>()

                val weather = openWeatherApiService.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude
                ).toWeatherUiModel()

                Resource.Success(weather)
            } catch (exception: HttpException) {
                Resource.Error(exception.localizedMessage ?: "An unexpected error occurred")
            } catch (exception: IOException) {
                Resource.Error("Could not reach server. Check your internet connection")
            }
        }
    }

    override suspend fun getWeatherWithForecast(
        latitude: Double,
        longitude: Double
    ): Flow<Resource<WeatherWithForecast?>> = flow {
        emit(Resource.Loading())

        val cached = getCachedWeather(latitude, longitude)
        emit(Resource.Success(cached))

        if (shouldFetchFromNetwork(cached?.current?.lastUpdated)) {
            try {
                val weatherWithForecast = fetchAndStoreWeather(latitude, longitude)
                emit(Resource.Success(weatherWithForecast))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(Resource.Error("Could not reach server. Check your internet connection"))
            }
        }
    }.flowOn(Dispatchers.IO) // ensure all DB/network work is off main

    private suspend fun getCachedWeather(latitude: Double, longitude: Double): WeatherWithForecast? =
        weatherWithForecastDao.getWeatherWithForecast(latitude, longitude)

    private suspend fun fetchAndStoreWeather(latitude: Double, longitude: Double): WeatherWithForecast? = coroutineScope {
        val currentDeferred = async { fetchCurrentWeather(latitude, longitude) }
        val forecastDeferred = async { fetchFiveDayForecast(latitude, longitude) }

        val currentWeather = currentDeferred.await()
        val forecastEntries = forecastDeferred.await()
            .list
            .onePerDay()
            .map { it.toForeCastEntity(latitude, longitude) }

        weatherWithForecastDao.addCurrentWeatherWithForecast(
            weatherEntity = currentWeather.toWeatherEntity(latitude, longitude),
            forecasts = forecastEntries
        )

        weatherWithForecastDao.getWeatherWithForecast(latitude, longitude)
    }

    private suspend fun fetchCurrentWeather(latitude: Double, longitude: Double) =
        openWeatherApiService.getCurrentWeather(latitude.toInt().toString(), longitude.toInt().toString())

    private suspend fun fetchFiveDayForecast(latitude: Double, longitude: Double) =
        openWeatherApiService.getFiveDayWeatherForecast(latitude.toInt().toString(), longitude.toInt().toString())


//    override suspend fun getWeatherWithForecast(latitude: Double, longitude: Double): Flow<Resource<WeatherWithForecast?>> =
//        flow {
//            coroutineScope {
//                try {
//                    emit(Resource.Loading())
//                    val cached = weatherWithForecastDao.getWeatherWithForecast(latitude = latitude, longitude = longitude)
//                    emit(Resource.Success(cached)) // immediate local display
//
//                    if (shouldFetchFromNetwork(cached?.current?.lastUpdated)) {
//                        val currentDay = async {
//                            openWeatherApiService.getCurrentWeather(
//                                latitude = latitude.toInt().toString(),
//                                longitude = longitude.toInt().toString()
//                            )
//                        }
//
//                        val fiveDayForecast = async {
//                            openWeatherApiService.getFiveDayWeatherForecast(
//                                latitude = latitude.toInt().toString(),
//                                longitude = longitude.toInt().toString()
//                            )
//                        }
//
//                        val forecastEntries = fiveDayForecast.await()
//                            .list
//                            .onePerDay()
//                            .map {
//                                it.toForeCastEntity(
//                                    latitude = latitude,
//                                    longitude = longitude,
//                                )
//                            }
//
//                        weatherWithForecastDao.addCurrentWeatherWithForecast(
//                            weatherEntity = currentDay.await().toWeatherEntity(
//                                latitude = latitude,
//                                longitude = longitude
//                            ),
//                            forecasts = forecastEntries
//                        )
//
//                        emit(
//                            Resource.Success(weatherWithForecastDao.getWeatherWithForecast(latitude = latitude, longitude = longitude))
//                        )
//                    }
//                } catch (exception: HttpException) {
//                    emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred"))
//                } catch (exception: IOException) {
//                    emit(Resource.Error("Could not reach server. Check your internet connection"))
//                }
//            }
//        }.flowOn(Dispatchers.IO)

    override fun getFavouriteLocations(): Flow<List<WeatherUiModel>> {
        return weatherDao
            .getFavouriteLocations()
            .map { it.map { it.toWeatherUiModel() } }
    }

    override suspend fun getLocationName(
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val geocoder = Geocoder(context)
            val addresses: List<Address?>? = withContext(Dispatchers.IO) {
                geocoder.getFromLocation(latitude, longitude, 1) // blocking on older APIs
            }

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                "${address?.locality ?: address?.subAdminArea}, ${address?.countryName}"
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shouldFetchFromNetwork(lastUpdated: Long?): Boolean {
        val now = System.currentTimeMillis()
        val cacheTimeout = 1000 * 60 * 5 // 5 minutes
        return lastUpdated == null || (now - lastUpdated) > cacheTimeout
    }

}