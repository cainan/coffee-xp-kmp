package com.cso.coffeexp.di

import com.cso.coffeexp.database.DatabaseFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformDatabaseModule = module {
    singleOf(::DatabaseFactory)
}