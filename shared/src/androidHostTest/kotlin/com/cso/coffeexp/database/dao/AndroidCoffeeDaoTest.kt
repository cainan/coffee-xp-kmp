package com.cso.coffeexp.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cso.coffeexp.database.CoffeeXpDatabase
import com.cso.coffeexp.database.CoffeeXpDatabaseConstructor
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AndroidCoffeeDaoTest : CoffeeDaoContractTest() {

    override fun createDatabase(): CoffeeXpDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder<CoffeeXpDatabase>(context) {
            CoffeeXpDatabaseConstructor.initialize()
        }
            .build()
    }
}
