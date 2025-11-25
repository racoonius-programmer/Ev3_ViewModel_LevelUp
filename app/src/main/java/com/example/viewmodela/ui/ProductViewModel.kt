package com.example.viewmodela.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viewmodela.api.Product
import com.example.viewmodela.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState(isLoading = true)
            try {
                // ¡CAMBIO CLAVE! Obtenemos la lista de productos directamente
                val products = RetrofitInstance.api.getProducts()
                _uiState.value = ProductUiState(products = products)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products", e)
                _uiState.value = ProductUiState(error = "No se pudieron cargar los productos. Causa: ${e.message}")
            }
        }
    }

    init {
        fetchProducts()
    }
}