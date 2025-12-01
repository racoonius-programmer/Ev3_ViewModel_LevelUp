package com.example.viewmodela.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<ProductoEntity>)

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun count(): Int
    
    @Query("DELETE FROM productos")
    suspend fun deleteAll()
}