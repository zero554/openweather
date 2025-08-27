package com.example.openweather

import android.app.Application
import com.example.openweather.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class OpenWeatherApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@OpenWeatherApp)
            modules(module)
        }
    }
}