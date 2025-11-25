package com.example.viewmodela.api

import retrofit2.http.GET

interface ApiService {
    /**
     * Obtiene la lista de productos directamente.
     */
    @GET("api/productos")
    suspend fun getProducts(): List<Product>
}