package com.example.openweather.data.remote

import com.example.openweather.common.Constants.API_KEY
import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherApiService {
    @GET("data/2.5/forecast")
    suspend fun getFiveDayWeatherForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherDto

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto
}