package com.example.viewmodela.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.viewmodela.api.Product
import com.example.viewmodela.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController) {
    // Recuperamos el producto pasado por savedStateHandle
    val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (product != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite scroll si el contenido es largo
            ) {
                // Imagen grande con manejo de error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.LightGray), // Fondo gris
                    contentAlignment = Alignment.Center
                ) {
                     AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imagenUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        error = rememberVectorPainter(Icons.Default.BrokenImage) // Icono si falla
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    // Lógica SKU/ID
                    val skuDisplay = if (!product.codigo.isNullOrBlank()) {
                        "SKU: ${product.codigo}"
                    } else {
                        "ID: ${product.id}"
                    }
                    
                    Text(skuDisplay, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(product.nombre ?: "Sin Nombre", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    
                    // Categoría
                    if (!product.categoria.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        SuggestionChip(
                            onClick = { },
                            label = { Text(product.categoria) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // USO DE FORMATTERS.KT AQUÍ
                    val precioText = product.precio?.let { "$ ${it.formatPrice()}" } ?: "Precio no disponible"
                    Text(precioText, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.descripcion ?: "No hay descripción disponible.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Stock por Sucursal
                    if (!product.stock.isNullOrEmpty()) {
                        Text("Stock en Sucursales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                product.stock.forEach { (sucursal, cantidad) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(sucursal, style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            text = if (cantidad > 0) "$cantidad un." else "Sin stock",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (cantidad > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                                        )
                                    }
                                    if (sucursal != product.stock.keys.last()) {
                                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                                    }
                                }
                            }
                        }
                    } else {
                        Text("Información de stock no disponible.", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error al cargar el producto")
            }
        }
    }
}
