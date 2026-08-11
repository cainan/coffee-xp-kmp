package com.cso.coffeexp.data.repository

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.database.CoffeeXpDatabaseConstructor

class DesktopOfflineFirstCoffeeRepositoryTest : OfflineFirstCoffeeRepositoryContractTest() {
    override fun createDatabase(): CoffeeXpDatabase =
        Room.inMemoryDatabaseBuilder<CoffeeXpDatabase> {
            CoffeeXpDatabaseConstructor.initialize()
        }
            .setDriver(BundledSQLiteDriver())
            .build()
}
