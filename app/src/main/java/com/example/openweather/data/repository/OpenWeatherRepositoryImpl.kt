package com.example.openweather.data.repository

import com.example.openweather.common.Resource
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
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class OpenWeatherRepositoryImpl(
    private val openWeatherApiService: OpenWeatherApiService,
    private val weatherWithForecastDao: WeatherWithForecastDao,
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

    override suspend fun getWeatherWithForecast(latitude: Double, longitude: Double): Flow<Resource<WeatherWithForecast?>> =
        flow {
            coroutineScope {
                try {
                    emit(Resource.Loading())
                    val cached = weatherWithForecastDao.getWeatherWithForecast(latitude = latitude, longitude = longitude)
                    emit(Resource.Success(cached)) // immediate local display

                    if (shouldFetchFromNetwork(cached?.current?.lastUpdated)) {
                        val currentDay = async {
                            openWeatherApiService.getCurrentWeather(
                                latitude = latitude.toInt().toString(),
                                longitude = longitude.toInt().toString()
                            )
                        }

                        val fiveDayForecast = async {
                            openWeatherApiService.getFiveDayWeatherForecast(
                                latitude = latitude.toInt().toString(),
                                longitude = longitude.toInt().toString()
                            )
                        }

                        val forecastEntries = fiveDayForecast.await()
                            .list
                            .onePerDay()
                            .map {
                                it.toForeCastEntity(
                                    latitude = latitude,
                                    longitude = longitude,
                                )
                            }

                        weatherWithForecastDao.addCurrentWeatherWithForecast(
                            weatherEntity = currentDay.await().toWeatherEntity(
                                latitude = latitude,
                                longitude = longitude
                            ),
                            forecasts = forecastEntries
                        )

                        emit(
                            Resource.Success(weatherWithForecastDao.getWeatherWithForecast(latitude = latitude, longitude = longitude))
                        )
                    }
                } catch (exception: HttpException) {
                    emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred"))
                } catch (exception: IOException) {
                    emit(Resource.Error("Could not reach server. Check your internet connection"))
                }
            }
        }

    fun shouldFetchFromNetwork(lastUpdated: Long?): Boolean {
        val now = System.currentTimeMillis()
        val cacheTimeout = 1000 * 60 * 5 // 5 minutes
        return lastUpdated == null || (now - lastUpdated) > cacheTimeout
    }

}