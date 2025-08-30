package com.example.openweather.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.openweather.presentation.models.navigation.NavigationDrawerItem
import com.example.openweather.presentation.models.scaffold.LocalScaffoldViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainUiState: MainUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    events: (MainEvent) -> Unit,
    content: @Composable (Modifier) -> Unit
) = with(mainUiState) {
    val scaffoldViewState = LocalScaffoldViewState.current
    val localHapticFeedback = LocalHapticFeedback.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val route = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerContent(items = navigationDrawerItems) {
                    events(MainEvent.NavigationDrawerItemClick(it))
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                scaffoldViewState.topAppBarState?.let {
                    with(it) {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    icon?.let {
                                        IconButton(onClick = {
                                            scope.launch { drawerState.open() }
                                        }) {
                                            Icon(
                                                imageVector = it,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground,
                                                modifier = Modifier
                                            )
                                        }
                                    }

                                    Text(
                                        text = title,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            navigationIcon = {
                                if (showNavIcon) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)

                                        Icon(
                                            imageVector = navIcon,
                                            contentDescription = "Navigate back"
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (actions.isNotEmpty()) {
                                    actions.forEach { topAppBarAction ->
                                        IconButton(
                                            onClick = { topAppBarAction.onClick() }
                                        ) {
                                            Icon(
                                                imageVector = topAppBarAction.icon,
                                                contentDescription = "Top app bar action icon",
                                                tint = topAppBarAction.tint
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            },
            modifier = modifier
        ) {
            content(Modifier.padding(it))
        }
    }
}

@Composable
fun ColumnScope.NavigationDrawerContent(
    items: ImmutableList<NavigationDrawerItem>,
    modifier: Modifier = Modifier,
    onItemClick: (NavigationDrawerItem) -> Unit
) {
    Text("Drawer title", modifier = Modifier.padding(16.dp))

    HorizontalDivider()

    items.forEach {
        NavigationDrawerItem(
            label = {
                Text(
                    text = it.title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            },
            icon = { Icon(imageVector = it.icon, contentDescription = null) },
            selected = it.isSelected,
            onClick = { onItemClick(it) },
            modifier = Modifier
                .padding(8.dp)
        )
    }
}