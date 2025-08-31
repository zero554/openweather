package com.example.openweather.presentation.ui.weather

sealed interface WeatherUiEvent {

    sealed interface TopAppBarAction: WeatherUiEvent {
        data object FavouriteClick: TopAppBarAction
    }
}