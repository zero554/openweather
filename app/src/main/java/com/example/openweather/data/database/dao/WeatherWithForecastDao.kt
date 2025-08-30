package com.example.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.database.entities.ForecastEntity
import com.example.openweather.data.database.entities.WeatherWithForecast

@Dao
interface WeatherWithForecastDao {

    @Upsert
    suspend fun insertWeather(weatherEntity: WeatherEntity)

    @Upsert
    suspend fun insertForecasts(forecasts: List<ForecastEntity>)

    @Query("SELECT * FROM weather WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getWeatherEntity(latitude: Double, longitude: Double): WeatherEntity?

    @Transaction
    @Query(
        """
            SELECT * FROM weather 
            WHERE latitude = :latitude AND longitude = :longitude
        """
    )
    suspend fun getWeatherWithForecast(latitude: Double, longitude: Double): WeatherWithForecast? {
        val weather = getWeatherEntity(latitude = latitude, longitude = longitude)
        val forecasts = getForecastsForWeather(latitude = latitude, longitude = longitude)

        return weather?.let {
            WeatherWithForecast(
                current = it,
                forecasts = forecasts
            )
        }
    }

    @Query(
        """
            SELECT * FROM forecasts 
            WHERE weatherLatitude = :latitude AND weatherLongitude = :longitude
        """
    )
    suspend fun getForecastsForWeather(latitude: Double, longitude: Double): List<ForecastEntity>


    @Transaction
    suspend fun addCurrentWeatherWithForecast(
        weatherEntity: WeatherEntity,
        forecasts: List<ForecastEntity>
    ) {
        // 1. Insert weather and get generated id
        insertWeather(weatherEntity)

        // 2. Insert all forecasts
        insertForecasts(
            forecasts.map {
                it.copy(
                    weatherLatitude = weatherEntity.latitude,
                    weatherLongitude = weatherEntity.longitude
                )
            }
        )
    }
}