package com.example.openweather.domain.usecase

import com.example.openweather.common.Resource
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.Location
import com.example.openweather.presentation.models.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

interface GetWeatherUseCase {
    suspend operator fun invoke(location: Location): Flow<Resource<Weather>>
}

class GetWeatherUseCaseImpl(
    private val openWeatherRepository: OpenWeatherRepository
) : GetWeatherUseCase {
    override suspend fun invoke(location: Location): Flow<Resource<Weather>> = flow {
        coroutineScope {
            emit(Resource.Loading())

            val currentDay = async {
                openWeatherRepository.getCurrentWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }

            val fiveDayForecast = async {
                openWeatherRepository.getFiveDayForecast(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }

            val errorMsg = listOfNotNull(
                (currentDay.await() as? Resource.Error)?.message,
                (fiveDayForecast.await() as? Resource.Error)?.message
            ).joinToString("\n").ifBlank { null }

            errorMsg?.let { emit(Resource.Error(it)) }

            if ((currentDay.await() as? Resource.Success)?.data != null && (fiveDayForecast.await() as? Resource.Success)?.data != null) {
                emit(
                    Resource.Success(
                        Weather(
                            current = (currentDay.await() as? Resource.Success)?.data!!,
                            fiveDayForecast = (fiveDayForecast.await() as? Resource.Success)?.data!!
                        )
                    )
                )
            }
        }
    }
}