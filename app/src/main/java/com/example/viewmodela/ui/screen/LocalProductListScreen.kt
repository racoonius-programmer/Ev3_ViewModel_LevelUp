package com.example.viewmodela.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viewmodela.ui.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalProductListScreen(
    vm: ProductViewModel,
    navController: NavController
) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            vm.clearMessage()
            // Opcional: Si se limpió la BD, podríamos volver atrás automáticamente.
            if (it.contains("limpiada")) {
                // navController.popBackStack() // Descomentar si se desea volver al home al limpiar
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos Locales") },
                actions = {
                    // Botón Eliminar Todo (Solo en Local)
                    IconButton(onClick = { 
                        vm.deleteAllData() 
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Todo", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    // Si el error es "No hay datos locales", lo mostramos amigablemente
                    ErrorState(uiState.error!!) { vm.loadFromDb() }
                }
                uiState.products.isEmpty() -> {
                    // Esto puede pasar si se borran todos los productos
                    Text("Base de datos vacía.", style = MaterialTheme.typography.bodyLarge)
                }
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(uiState.products) { product ->
                            ProductItem(product, onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                                navController.navigate("product_detail")
                            })
                        }
                    }
                }
            }
        }
    }
}
