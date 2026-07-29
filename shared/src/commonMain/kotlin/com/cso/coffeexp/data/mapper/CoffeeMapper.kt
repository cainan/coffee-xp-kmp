package com.cso.coffeexp.data.mapper

import com.cso.coffeexp.database.entity.CoffeeEntity
import com.cso.coffeexp.domain.model.Coffee
import kotlinx.datetime.LocalDate

fun CoffeeEntity.toCoffee(): Coffee = Coffee(
    id = id,
    imageUrl = imageUrl,
    name = name,
    roaster = roaster,
    series = series,
    origin = origin,
    process = process,
    elevation = elevation,
    roastDate = roastDate.toLocalDate(),
    roastLevel = roastLevel,
    brewingMethod = brewingMethod,
    grindSize = grindSize,
    temperature = temperature,
    ratio = ratio,
    brewTime = brewTime,
    rating = rating,
    notes = notes,
    createdAt = createdAt.toLocalDate(),
    lastModifiedAt = lastModifiedAt.toLocalDate(),
)

fun Coffee.toCoffeeEntity(): CoffeeEntity = CoffeeEntity(
    id = id ?: 0,
    imageUrl = imageUrl,
    name = name,
    roaster = roaster,
    series = series,
    origin = origin,
    process = process,
    elevation = elevation,
    roastDate = roastDate.toEpochDays(),
    roastLevel = roastLevel,
    brewingMethod = brewingMethod,
    grindSize = grindSize,
    temperature = temperature,
    ratio = ratio,
    brewTime = brewTime,
    rating = rating,
    notes = notes,
    createdAt = createdAt.toEpochDays(),
    lastModifiedAt = lastModifiedAt.toEpochDays(),
)

private fun Long.toLocalDate(): LocalDate = LocalDate.fromEpochDays(this)