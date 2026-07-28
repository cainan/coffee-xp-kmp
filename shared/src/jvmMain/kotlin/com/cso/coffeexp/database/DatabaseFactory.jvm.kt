package com.cso.coffeexp.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.cso.coffeexp.core.util.appDataDirectory
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<CoffeeXpDatabase> {
        val directory = appDataDirectory

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val dbFile = File(directory, CoffeeXpDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}