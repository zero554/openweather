package com.example.openweather.presentation.ui.main

import com.example.openweather.BaseViewModel
import com.example.openweather.presentation.models.navigation.NavigationDrawerItem
import com.example.openweather.presentation.models.navigation.NavigationEvent
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : BaseViewModel() {

    private val _mainUiState: MutableStateFlow<MainUiState?> = MutableStateFlow(null)
    val mainUiState = _mainUiState.asStateFlow()

    init {
        _mainUiState.value = MainUiState(
            navigationDrawerItems = getNavigationDrawerItems().toPersistentList(),
        )
    }

    fun handleMainEvents(event: MainEvent) {
        when (event) {
            is MainEvent.NavigationDrawerItemClick -> {
                postUiEvent(getNavigationDrawerEvent(event.item))
            }
        }
    }

    private fun getNavigationDrawerEvent(selectedNavigationDrawerItem: NavigationDrawerItem): NavigationEvent.NavigationDrawerEvent {
        return when (selectedNavigationDrawerItem) {
            NavigationDrawerItem.FAVOURITES -> NavigationEvent.NavigationDrawerEvent.Favourites
        }
    }

    private fun getNavigationDrawerItems() = NavigationDrawerItem
        .entries
        .toPersistentList()
}