package com.example.openweather.data.repository

import com.example.openweather.common.Constants.API_KEY
import com.example.openweather.data.remote.OpenWeatherApiService
import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto
import com.example.openweather.domain.repository.OpenWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenWeatherRepositoryImpl(
    private val openWeatherApiService: OpenWeatherApiService
): OpenWeatherRepository {
    override suspend fun getFiveDayForecast(): WeatherDto {
        return withContext(Dispatchers.IO) {
            openWeatherApiService.getFiveDayWeatherForecast(
                latitude = "35",
                longitude = "18"
            )
        }

    }

    override suspend fun getCurrentWeather(): CurrentWeatherDto {
        return withContext(Dispatchers.IO) {
            openWeatherApiService.getCurrentWeather(
                latitude = "10",
                longitude = "18"
            )
        }
    }
}