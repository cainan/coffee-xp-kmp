package com.cso.coffeexp.di

import com.cso.coffeexp.database.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformDatabaseModule = module {
    single { DatabaseFactory(androidContext()) }
}