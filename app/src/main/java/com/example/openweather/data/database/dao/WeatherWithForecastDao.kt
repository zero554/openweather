package com.example.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.database.entities.ForecastEntity

@Dao
interface WeatherWithForecastDao {

    @Upsert
    suspend fun insertWeather(weatherEntity: WeatherEntity): Long

    @Upsert
    suspend fun insertForecasts(payments: List<ForecastEntity>)

    @Transaction
    suspend fun addCurrentWeatherWithForecast(
        weatherEntity: WeatherEntity,
        forecasts: List<ForecastEntity>
    ) {
        // 1. Insert weather and get generated id
        val weatherId = insertWeather(weatherEntity)

        // 2. Copy weather with the generated id
        val weatherWithId = weatherEntity.copy(id = weatherId)

        // 4. Insert all scheduled payments
        insertForecasts(forecasts.map { it.copy(weatherId = weatherWithId.id) })
    }
}