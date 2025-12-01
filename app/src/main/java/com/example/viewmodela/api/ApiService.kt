package com.example.viewmodela.api

import retrofit2.http.GET

interface ApiService {
    /**
     * Obtiene la lista de productos desde productos.json
     */
    @GET("productos.json")
    suspend fun getProducts(): List<Product>
}