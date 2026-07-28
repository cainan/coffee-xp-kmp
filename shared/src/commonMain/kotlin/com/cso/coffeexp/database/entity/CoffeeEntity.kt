package com.cso.coffeexp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee")
data class CoffeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val imageUrl: String?,
    val name: String,
    val roaster: String,
    val series: String?,
    val origin: String,
    val process: String?,
    val elevation: String?,
    val roastDate: Long,
    val roastLevel: String,
    val brewingMethod: String,
    val grindSize: String?,
    val temperature: Int?,
    val ratio: String?,
    val brewTime: String?,
    val rating: Double,
    val notes: String?,
    val createdAt: Long,
    val lastModifiedAt: Long,
)