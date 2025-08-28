package com.example.openweather.di

import com.example.openweather.common.Constants.BASE_URL
import com.example.openweather.data.remote.OpenWeatherApiService
import com.example.openweather.data.repository.OpenWeatherRepositoryImpl
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.domain.usecase.GetCurrentLocationUseCase
import com.example.openweather.domain.usecase.GetCurrentLocationUseCaseImpl
import com.example.openweather.domain.usecase.GetWeatherUseCase
import com.example.openweather.domain.usecase.GetWeatherUseCaseImpl
import com.example.openweather.presentation.ui.main.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val module = module {
    single<OpenWeatherApiService> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(OpenWeatherApiService::class.java)
    }

    singleOf(::OpenWeatherRepositoryImpl).bind<OpenWeatherRepository>()
    singleOf(::GetCurrentLocationUseCaseImpl).bind<GetCurrentLocationUseCase>()
    singleOf(::GetWeatherUseCaseImpl).bind<GetWeatherUseCase>()

    viewModelOf(::WeatherViewModel)
}