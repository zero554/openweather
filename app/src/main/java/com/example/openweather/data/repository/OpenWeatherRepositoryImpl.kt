package com.example.openweather.data.repository

import com.example.openweather.common.Resource
import com.example.openweather.data.remote.OpenWeatherApiService
import com.example.openweather.domain.mappers.onePerDay
import com.example.openweather.domain.mappers.toForeCastUiModel
import com.example.openweather.domain.mappers.toWeatherUiModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.CurrentWeatherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class OpenWeatherRepositoryImpl(
    private val openWeatherApiService: OpenWeatherApiService
) : OpenWeatherRepository {
    override suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): Resource<ImmutableList<ForecastUiModel>> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Loading<CurrentWeatherUiModel>()

                val fiveDayForecast = openWeatherApiService.getFiveDayWeatherForecast(
                    latitude = latitude,
                    longitude = longitude
                ).list
                .onePerDay()
                .map { it.toForeCastUiModel() }
                .toPersistentList()

                Resource.Success(data = fiveDayForecast)
            } catch (exception: HttpException) {
                Resource.Error(exception.localizedMessage ?: "An unexpected error occurred")
            } catch (exception: IOException) {
                Resource.Error("Could not reach server. Check your internet connection ${exception.localizedMessage}")
            }
        }

    }

    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Resource<CurrentWeatherUiModel> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Loading<CurrentWeatherUiModel>()

                val weather = openWeatherApiService.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude
                ).toWeatherUiModel()

                Resource.Success(weather)
            } catch (exception: HttpException) {
                Resource.Error(exception.localizedMessage ?: "An unexpected error occurred")
            } catch (exception: IOException) {
                Resource.Error("Could not reach server. Check your internet connection")
            }
        }
    }
}