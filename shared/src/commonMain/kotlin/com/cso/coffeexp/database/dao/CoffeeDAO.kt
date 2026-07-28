package com.cso.coffeexp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cso.coffeexp.database.entity.CoffeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDAO {

    @Query("SELECT * FROM coffee ORDER BY createdAt DESC, id DESC")
    fun observeAll(): Flow<List<CoffeeEntity>>

    @Query("SELECT * FROM coffee WHERE id = :id")
    suspend fun getById(id: Long): CoffeeEntity?

    @Upsert
    suspend fun upsert(coffee: CoffeeEntity): Long

    @Query("DELETE FROM coffee WHERE id = :id")
    suspend fun deleteById(id: Long): Int

}
