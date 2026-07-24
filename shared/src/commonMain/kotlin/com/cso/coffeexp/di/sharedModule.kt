package com.cso.coffeexp.di

import com.cso.coffeexp.data.coffee.InMemoryCoffeeRepository
import com.cso.coffeexp.data.logger.KermitLogger
import com.cso.coffeexp.domain.logger.CoffeeXpLogger
import com.cso.coffeexp.domain.repository.CoffeeRepository
import com.cso.coffeexp.presentation.details.DetailsViewModel
import com.cso.coffeexp.presentation.home.HomeViewModel
import com.cso.coffeexp.presentation.new_coffee.NewCoffeeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NewCoffeeViewModel)
    viewModelOf(::DetailsViewModel)
    single<CoffeeXpLogger> { KermitLogger() }
    single<CoffeeRepository> { InMemoryCoffeeRepository() }
}