package com.example.openweather.domain.usecase

import com.example.openweather.common.Resource
import com.example.openweather.domain.mappers.toForecastUiModel
import com.example.openweather.domain.mappers.toWeatherUiModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.Location
import com.example.openweather.presentation.models.Weather
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface GetWeatherUseCase {
    suspend operator fun invoke(location: Location): Flow<Resource<Weather>>
}

class GetWeatherUseCaseImpl(
    private val openWeatherRepository: OpenWeatherRepository
) : GetWeatherUseCase {
    override suspend fun invoke(location: Location): Flow<Resource<Weather>> {
        return openWeatherRepository.getWeatherWithForecast(
            latitude = location.latitude,
            longitude = location.longitude
        ).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val weatherWithForecast = resource.data
                    if (weatherWithForecast != null) {
                        Resource.Success(
                            Weather(
                                current = weatherWithForecast.current.toWeatherUiModel(),
                                fiveDayForecast = weatherWithForecast.forecasts.map { it.toForecastUiModel() }
                                    .toPersistentList()
                            )
                        )
                    } else {
                        Resource.Error<Weather>("No weather data available")
                    }
                }
                is Resource.Error -> {
                    // convert the generic type to Resource<Weather>
                    Resource.Error<Weather>(resource.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Resource.Loading<Weather>()
                }
            }
        }
    }
}