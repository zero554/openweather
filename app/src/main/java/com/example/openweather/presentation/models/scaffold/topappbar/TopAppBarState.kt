package com.example.openweather.presentation.models.scaffold.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TopAppBarState(
    val title: String = "",
    val icon: ImageVector? = null,
    val navIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    val showNavIcon: Boolean = false,
    val actions: ImmutableList<TopBarAction> = persistentListOf()
)
