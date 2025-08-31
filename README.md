# Android Mini Project

### Table of Contents
- [Description](#description)
- [Approach](#approach)
- [Architecture Diagram](#architecture-diagram)
- [How To Use](#how-to-use)

## Description

Android project that simulates a Weather App built using **Jetpack Compose**. The app shows:

- The **current weather** for the user's location.
- A **five-day forecast** for both the city of Cape Town and the user's current location.
- The ability to **favorite weather locations** and access them through a **Navigation Drawer**.

The implementation uses the [MVVM](https://developer.android.com/jetpack/guide#recommended-app-arch) architecture to separate app logic into layers for maintainability and scalability. The app is fully Compose-based, including UI components like the top bar, navigation drawer, lists, and detail views.

---

## Approach

- Weather data is retrieved from the [OpenWeather API](https://openweathermap.org/api) and stored in a **Room** database as the single source of truth.
- The **Repository** handles data flow: it decides whether to fetch from the API or database based on API success, always keeping the database as the authoritative source.
- **Single-Activity architecture** with **Composable screens**:
    - **Overview Screen**: Displays a vertical list of weather forecasts.
    - **Detail Screen**: Shows detailed weather information for a selected forecast item.
- **Current Location Support**: Uses the device's GPS to fetch the user's current location and display both current weather and a five-day forecast.
- **Favorites & Navigation Drawer**:
    - Locations can be favorited using a **favorite icon** on the top app bar.
    - Favorite locations are accessible via a **Navigation Drawer**, opened through a **menu icon** on the top app bar.
- **Compose UI Features**: Includes `LazyColumn` for lists, `TopAppBar` for navigation actions, and `Scaffold` for handling the drawer and content layout.
- Unit tests for **ViewModels** are included to verify business logic.

---

## Architecture Diagram

```mermaid
flowchart TD
    A[MainActivity (Scaffold)] --> B[TopAppBar]
    B --> B1[Menu Icon -> Open Navigation Drawer]
    B --> B2[Favorite Icon -> Add Current Location]
    A --> C[Navigation Drawer]
    C --> C1[Favorite Locations List]
    A --> D[NavHost / Screens]
    D --> E[WeatherScreen]
    D --> F[FavouritesScreen]

    E --> G[LazyColumn of Forecast Items]
    G --> H[Forecast Item (Composable)]

    Repository -->|Fetch/Store| RoomDatabase
    Repository -->|Fetch| OpenWeatherAPI
    ViewModel --> Repository
    E --> ViewModel
    F --> ViewModel
