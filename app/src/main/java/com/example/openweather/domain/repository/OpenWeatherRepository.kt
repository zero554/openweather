package com.example.openweather.domain.repository

import com.example.openweather.common.Resource
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.database.entities.WeatherWithForecast
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface OpenWeatherRepository {

    suspend fun insertWeather(weatherEntity: WeatherEntity)

    suspend fun getFiveDayForecast(latitude: String, longitude: String): Resource<ImmutableList<ForecastUiModel>>

    suspend fun getCurrentWeather(latitude: String, longitude: String): Resource<WeatherUiModel>

    suspend fun getWeatherWithForecast(latitude: Double, longitude: Double): Flow<Resource<WeatherWithForecast?>>

    fun getFavouriteLocations(): Flow<List<WeatherUiModel>>

    suspend fun getLocationName(latitude: Double, longitude: Double): String?
}