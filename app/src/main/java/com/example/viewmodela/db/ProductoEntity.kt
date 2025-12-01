package com.example.viewmodela.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String?,
    val nombre: String,
    val precio: Int,
    val imagenUrl: String,
    val categoria: String?,
    val descripcion: String?
)