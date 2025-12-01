package com.example.viewmodela.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.viewmodela.api.Product
import com.example.viewmodela.ui.ProductViewModel
import com.example.viewmodela.util.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiProductListScreen(
    vm: ProductViewModel,
    navController: NavController
) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            vm.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados API") },
                actions = {
                    // Botón Refrescar API
                    IconButton(onClick = { 
                        if (NetworkUtils.isInternetAvailable(context)) {
                            vm.loadFromApi()
                            Toast.makeText(context, "Recargando...", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Sin conexión a Internet", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar API")
                    }

                    // Botón Guardar Local (Solo tiene sentido aquí)
                    Button(
                        onClick = { vm.saveToDb() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar Local")
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
                    ErrorState(uiState.error!!, onRetry = { vm.loadFromApi() })
                }
                uiState.products.isEmpty() -> {
                    Text("No hay datos de la API.", style = MaterialTheme.typography.bodyLarge)
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

// --- Componentes Compartidos (Copiados aquí para evitar errores de referencia cruzada rápida) ---

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    // Lógica para el SKU: Si codigo es nulo, usar ID
    val skuDisplay = if (!product.codigo.isNullOrBlank()) {
        "SKU: ${product.codigo}"
    } else {
        "ID: ${product.id}"
    }

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.nombre ?: "Producto",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(Icons.Default.BrokenImage)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(skuDisplay, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Text(product.nombre ?: "Sin Nombre", style = MaterialTheme.typography.titleMedium)
                val precioText = product.precio?.let { String.format("$%d", it) } ?: "$0"
                Text(precioText, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
