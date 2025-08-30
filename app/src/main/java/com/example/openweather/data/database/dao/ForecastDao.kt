package com.example.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.openweather.data.database.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Upsert
    suspend fun upsertForecastDao(forecastEntity: ForecastEntity)

    @Delete
    suspend fun deleteForecast(forecastEntity: ForecastEntity)

    @Query("SELECT * FROM forecasts WHERE id = :id")
    suspend fun getForecastById(id: Long): ForecastEntity?

    @Query("SELECT * FROM forecasts")
    fun getAllForecastedWeather(): Flow<List<ForecastEntity>>

    @Query("SELECT * FROM forecasts WHERE weatherLatitude = :latitude AND weatherLongitude = :longitude ORDER BY lastUpdated ASC")
    fun getForecastsForWeather(latitude: Double, longitude: Double): Flow<List<ForecastEntity>>
}