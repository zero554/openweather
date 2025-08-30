package com.example.openweather.data.database.entities

data class WeatherWithForecast(
    val current: WeatherEntity,
    val forecasts: List<ForecastEntity>
)
