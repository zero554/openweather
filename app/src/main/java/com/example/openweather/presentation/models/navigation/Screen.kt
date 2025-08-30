package com.example.openweather.presentation.models.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    @SerialName(value = "favourites")
    data object Favourites: Screen

    @Serializable
    @SerialName(value = "weather")
    data object Weather: Screen
}


