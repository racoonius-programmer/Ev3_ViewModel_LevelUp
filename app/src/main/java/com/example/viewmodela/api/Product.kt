package com.example.viewmodela.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Modelo que representa un único producto.
 * Incluye 'id' para manejar el identificador local de la base de datos.
 * Se han agregado campos para soportar la estructura completa del JSON.
 */
data class Product(
    val id: Int = 0, // ID local de la base de datos (0 si viene de la API)
    @SerializedName("sku") val codigo: String?, // El JSON usa "sku", mapeamos a codigo
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("imagen") val imagenUrl: String?,
    @SerializedName("precio") val precio: Int?,
    @SerializedName("categoria") val categoria: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("stock_por_sucursal") val stock: Map<String, Int>?
) : Serializable
