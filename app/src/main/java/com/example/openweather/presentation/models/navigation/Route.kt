package com.example.openweather.presentation.models.navigation

import kotlinx.serialization.Serializable

interface Route

@Serializable
data object WeatherRoute: Route