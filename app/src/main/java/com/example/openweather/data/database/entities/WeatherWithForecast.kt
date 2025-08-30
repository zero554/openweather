package com.example.openweather.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithForecast(
    @Embedded val current: WeatherEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherId"
    )
    val forecasts: List<ForecastEntity>
)
