package com.cso.coffeexp.data.mapper

import com.cso.coffeexp.core.utils.LocalDate
import com.cso.coffeexp.core.utils.toEpochMillisString
import com.cso.coffeexp.database.entity.CoffeeEntity
import com.cso.coffeexp.domain.model.Coffee

fun CoffeeEntity.toCoffee(): Coffee = Coffee(
    id = id,
    imageUrl = imageUrl,
    name = name,
    roaster = roaster,
    series = series,
    origin = origin,
    process = process,
    elevation = elevation,
    roastDate = LocalDate(roastDate.toLong()),
    roastLevel = roastLevel,
    brewingMethod = brewingMethod,
    grindSize = grindSize,
    temperature = temperature,
    ratio = ratio,
    brewTime = brewTime,
    rating = rating,
    notes = notes,
    createdAt = LocalDate(createdAt.toLong()),
    lastModifiedAt = LocalDate(lastModifiedAt.toLong()),
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
    roastDate = roastDate.toEpochMillisString(),
    roastLevel = roastLevel,
    brewingMethod = brewingMethod,
    grindSize = grindSize,
    temperature = temperature,
    ratio = ratio,
    brewTime = brewTime,
    rating = rating,
    notes = notes,
    createdAt = roastDate.toEpochMillisString(),
    lastModifiedAt = roastDate.toEpochMillisString(),
)