package com.example.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.database.entities.WeatherWithForecast
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Upsert
    suspend fun upsertWeather(current: WeatherEntity)

    @Delete
    suspend fun deleteWeather(current: WeatherEntity)

    @Transaction
    @Query("SELECT * FROM weather WHERE id = :id")
    suspend fun getWeatherWithForecast(id: Long): WeatherWithForecast?

    @Query("SELECT * FROM weather")
    fun getAllWeather(): Flow<List<WeatherEntity>>
}