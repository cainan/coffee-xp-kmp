package com.cso.coffeexp

import android.app.Application
import com.cso.coffeexp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin{
            androidContext(this@MainApplication)
        }
    }
}