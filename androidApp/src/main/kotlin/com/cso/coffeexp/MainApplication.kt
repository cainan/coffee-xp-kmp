package com.cso.coffeexp

import android.app.Application
import com.cso.coffeexp.di.initKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}