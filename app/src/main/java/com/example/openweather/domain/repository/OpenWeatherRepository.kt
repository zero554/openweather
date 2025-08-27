package com.example.openweather.domain.repository

import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto

interface OpenWeatherRepository {

    suspend fun getFiveDayForecast(): WeatherDto

    suspend fun getCurrentWeather(): CurrentWeatherDto
}