package com.example.openweather.presentation.models.navigation

interface NavigationEvent {
    data class LocationClick(val latitude: Double, val longitude: Double): NavigationEvent

    data object NavigateBack: NavigationEvent

    sealed interface NavigationDrawerEvent {
        data object Favourites: NavigationDrawerEvent
    }
}