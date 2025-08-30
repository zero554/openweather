package com.example.openweather.presentation.models.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationDrawerItem(
    val title: String,
    val icon: ImageVector,
    val isSelected: Boolean = false
) {
    FAVOURITES(
        title = "Favourites",
        icon = Icons.Default.Favorite,
    );
}