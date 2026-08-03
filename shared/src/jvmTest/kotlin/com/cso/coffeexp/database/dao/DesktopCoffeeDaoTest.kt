package com.cso.coffeexp.database.dao

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.database.CoffeeXpDatabaseConstructor

class DesktopCoffeeDaoTest : CoffeeDaoContractTest() {

    override fun createDatabase(): CoffeeXpDatabase {
        return Room.inMemoryDatabaseBuilder<CoffeeXpDatabase> {
            CoffeeXpDatabaseConstructor.initialize()
        }
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}
