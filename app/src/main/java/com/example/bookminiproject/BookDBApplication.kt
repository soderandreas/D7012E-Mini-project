package com.example.bookminiproject

import android.app.Application
import com.example.bookminiproject.database.AppContainer
import com.example.bookminiproject.database.DefaultAppContainer

class BookDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}