package com.example.openweather.domain.repository

import com.example.openweather.common.Resource
import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList

interface OpenWeatherRepository {

    suspend fun getFiveDayForecast(latitude: String, longitude: String): Resource<ImmutableList<ForecastUiModel>>

    suspend fun getCurrentWeather(latitude: String, longitude: String): Resource<WeatherUiModel>
}