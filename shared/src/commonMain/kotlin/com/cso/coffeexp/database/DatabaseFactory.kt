package com.cso.coffeexp.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<CoffeeXpDatabase>
}