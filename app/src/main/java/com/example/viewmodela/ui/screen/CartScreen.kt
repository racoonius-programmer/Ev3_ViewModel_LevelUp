package com.example.viewmodela.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.viewmodela.model.Producto
import com.example.viewmodela.util.formatPrice

@Composable
fun CartScreen(cart: List<Producto>, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(10.dp))

        if (cart.isEmpty()) {
            Text("El carrito está vacío")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cart) { product ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (product.imageRes != null) {
                                Image(
                                    painter = painterResource(id = product.imageRes),
                                    contentDescription = product.name,
                                    modifier = Modifier.size(60.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (product.icon != null) {
                                Image(
                                    imageVector = product.icon,
                                    contentDescription = product.name,
                                    modifier = Modifier.size(60.dp)
                                )
                            } else {
                                // Placeholder in case both are null
                                Spacer(Modifier.size(60.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(product.name)
                                Text("Precio: $${product.price.formatPrice()}")
                            }
                            // --- Botones de Cantidad ---
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { /* TODO: Lógica para quitar */ }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Quitar")
                                }
                                Text("1") // Cantidad (actualmente estática)
                                IconButton(onClick = { /* TODO: Lógica para agregar */ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Button(onClick = onBack) { Text("Volver a productos")
        }
        Spacer(Modifier.height(35.dp))
    }
}
