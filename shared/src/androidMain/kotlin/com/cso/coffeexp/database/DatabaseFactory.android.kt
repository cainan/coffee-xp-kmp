package com.cso.coffeexp.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun create(): RoomDatabase.Builder<CoffeeXpDatabase> {
        val dbFile = context.applicationContext.getDatabasePath(CoffeeXpDatabase.DB_NAME)

        return Room.databaseBuilder(
            context.applicationContext,
            dbFile.absolutePath
        )
    }
}