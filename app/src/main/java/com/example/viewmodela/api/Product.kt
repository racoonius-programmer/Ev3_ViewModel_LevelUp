package com.example.viewmodela.api

import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa un único producto, basado en la entidad `Productos` de la API.
 */
data class Product(
    @SerializedName("codigo") val codigo: String,
    @SerializedName("nombre") val nombre: String,
    // El campo en la API se llama 'imagen', pero lo mapeamos a 'imagenUrl' para mayor claridad.
    @SerializedName("imagen") val imagenUrl: String,
    @SerializedName("precio") val precio: Int
)
