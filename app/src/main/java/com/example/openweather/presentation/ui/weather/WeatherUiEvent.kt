package com.example.openweather.presentation.ui.weather

import com.example.openweather.presentation.models.WeatherUiModel

sealed interface WeatherUiEvent {

    sealed interface TopAppBarAction: WeatherUiEvent {
        data class FavouriteClick(val weatherUiModel: WeatherUiModel): TopAppBarAction
    }
}