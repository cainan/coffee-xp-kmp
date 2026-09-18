package com.cso.coffeexp.di

import com.cso.coffeexp.data.logger.KermitLogger
import com.cso.coffeexp.data.repository.FileKitPhotoStorage
import com.cso.coffeexp.data.repository.OfflineFirstCoffeeRepository
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.repository.CoffeeRepository
import com.cso.coffeexp.domain.repository.PhotoStorage
import com.cso.coffeexp.presentation.details.DetailsViewModel
import com.cso.coffeexp.presentation.home.HomeViewModel
import com.cso.coffeexp.presentation.new_coffee.NewCoffeeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val APPLICATION_SCOPE = named("applicationScope")

val sharedModule = module {
    single<CoroutineScope>(APPLICATION_SCOPE) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewCoffeeViewModel)
    viewModelOf(::DetailsViewModel)
    single<CoffeeXpLogger> { KermitLogger() }
    single<CoffeeRepository> {
        OfflineFirstCoffeeRepository(
            db = get(),
            applicationScope = get(APPLICATION_SCOPE),
        )
    }
    single<PhotoStorage> { FileKitPhotoStorage() }
}
