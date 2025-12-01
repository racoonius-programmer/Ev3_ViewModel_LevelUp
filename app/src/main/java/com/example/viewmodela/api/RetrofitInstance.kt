package com.example.viewmodela.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // URL base del repositorio de GitHub proporcionado en el PDF
    private const val BASE_URL = "https://raw.githubusercontent.com/chalalo1533/ServicioRest/refs/heads/master/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}