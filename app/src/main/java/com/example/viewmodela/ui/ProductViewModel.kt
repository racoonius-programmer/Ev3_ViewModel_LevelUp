package com.example.viewmodela.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.viewmodela.api.Product
import com.example.viewmodela.api.RetrofitInstance
import com.example.viewmodela.db.ProductoDao
import com.example.viewmodela.db.ProductoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

class ProductViewModel(private val dao: ProductoDao) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    suspend fun hasLocalData(): Boolean {
        return withContext(Dispatchers.IO) {
            dao.count() > 0
        }
    }

    fun loadFromApi() {
        viewModelScope.launch {
            _uiState.value = ProductUiState(isLoading = true)
            try {
                val products = RetrofitInstance.api.getProducts()
                
                val productsWithTempIds = products.mapIndexed { index, product ->
                    product.copy(id = index + 1)
                }
                
                _uiState.value = ProductUiState(products = productsWithTempIds)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching from API", e)
                _uiState.value = ProductUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }

    fun loadFromDb() {
        viewModelScope.launch {
            _uiState.value = ProductUiState(isLoading = true)
            try {
                val entities = dao.getAll()
                if (entities.isEmpty()) {
                    _uiState.value = ProductUiState(error = "No hay datos locales almacenados")
                } else {
                    val products = entities.map { 
                        Product(
                            id = it.id, 
                            codigo = it.codigo, 
                            nombre = it.nombre, 
                            imagenUrl = it.imagenUrl, 
                            precio = it.precio,
                            categoria = it.categoria,
                            descripcion = it.descripcion,
                            stock = null // El stock no se persiste en BD local en esta versión simplificada
                        ) 
                    }
                    _uiState.value = ProductUiState(products = products)
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching from DB", e)
                _uiState.value = ProductUiState(error = "Error al leer BD: ${e.message}")
            }
        }
    }

    fun saveToDb() {
        val currentProducts = _uiState.value.products
        if (currentProducts.isEmpty()) {
             _uiState.value = _uiState.value.copy(error = "No hay productos para guardar")
             return
        }

        viewModelScope.launch {
            try {
                val entities = currentProducts.map { product ->
                    ProductoEntity(
                        codigo = product.codigo, 
                        nombre = product.nombre ?: "Sin nombre",
                        precio = product.precio ?: 0,
                        imagenUrl = product.imagenUrl ?: "",
                        categoria = product.categoria,
                        descripcion = product.descripcion
                    )
                }

                dao.deleteAll() 
                dao.insertAll(entities)
                _uiState.value = _uiState.value.copy(message = "Datos guardados localmente con éxito (${entities.size} productos)")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error saving to DB", e)
                _uiState.value = _uiState.value.copy(error = "Error al guardar: ${e.message}")
            }
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            try {
                dao.deleteAll()
                _uiState.value = ProductUiState(message = "Base de datos limpiada", products = emptyList())
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error deleting data", e)
                _uiState.value = _uiState.value.copy(error = "Error al limpiar BD: ${e.message}")
            }
        }
    }

    class Factory(private val dao: ProductoDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}