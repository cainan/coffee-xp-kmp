package com.cso.coffeexp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cso.coffeexp.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val databaseModule = module {
    includes(platformDatabaseModule)

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}