package com.example.openweather.domain.repository

import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto

interface OpenWeatherRepository {

    suspend fun getFiveDayForecast(latitude: String, longitude: String): WeatherDto

    suspend fun getCurrentWeather(latitude: String, longitude: String): CurrentWeatherDto
}