package com.example.openweather.data.repository

import com.example.openweather.data.remote.OpenWeatherApiService
import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto
import com.example.openweather.domain.repository.OpenWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenWeatherRepositoryImpl(
    private val openWeatherApiService: OpenWeatherApiService
): OpenWeatherRepository {
    override suspend fun getFiveDayForecast(latitude: String, longitude: String): WeatherDto {
        return withContext(Dispatchers.IO) {
            openWeatherApiService.getFiveDayWeatherForecast(
                latitude = latitude,
                longitude = longitude
            )
        }

    }

    override suspend fun getCurrentWeather(latitude: String, longitude: String): CurrentWeatherDto {
        return withContext(Dispatchers.IO) {
            openWeatherApiService.getCurrentWeather(
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}