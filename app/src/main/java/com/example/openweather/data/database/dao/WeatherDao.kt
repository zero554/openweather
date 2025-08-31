package com.example.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.openweather.data.database.entities.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Upsert
    suspend fun upsertWeather(current: WeatherEntity)

    @Delete
    suspend fun deleteWeather(current: WeatherEntity)

    @Query("SELECT * FROM weather WHERE isFavourite = 1")
    fun getFavouriteLocations(): Flow<List<WeatherEntity>>
}