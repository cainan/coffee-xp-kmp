package com.cso.coffeexp.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.cso.coffeexp.database.dao.CoffeeDAO
import com.cso.coffeexp.database.entity.CoffeeEntity

@Database(entities = [CoffeeEntity::class], version = 1)
@ConstructedBy(CoffeeXpDatabaseConstructor::class)
abstract class CoffeeXpDatabase : RoomDatabase() {
    abstract fun getCoffeeDao(): CoffeeDAO

    companion object {
        const val DB_NAME = "coffeexp.db"
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object CoffeeXpDatabaseConstructor : RoomDatabaseConstructor<CoffeeXpDatabase> {
    override fun initialize(): CoffeeXpDatabase
}