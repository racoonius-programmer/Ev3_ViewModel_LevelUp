package com.example.viewmodela.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.viewmodela.R
import com.example.viewmodela.model.Producto
import com.example.viewmodela.util.formatPrice
import com.example.viewmodela.viewmodel.UsuarioViewModel

// --- Modelos de datos ---
// Modificamos el modelo para que acepte un recurso de imagen o un ícono
data class CarouselSlide(
    val title: String,
    val subtitle: String,
    val imageRes: Int? = null,
    val icon: ImageVector? = null
)

// --- Datos de Ejemplo con Íconos e Imagen ---
val carouselSlides = listOf(
    CarouselSlide(
        title = "Level-up Gamer",
        subtitle = "Tu lugar para todo lo relacionado con videojuegos",
        imageRes = R.drawable.gamer_stock
    ),
    CarouselSlide(
        title = "Arma tu PC como un Pro",
        subtitle = "Componentes de última generación",
        imageRes = R.drawable.componentes
    ),
    CarouselSlide(
        title = "Conéctate al Mundo Gamer",
        subtitle = "Comunidad, merch y más",
        imageRes = R.drawable.evento
    )
)

val bestSellers = listOf(
    Producto("Polera Level Up", 15990, imageRes = R.drawable.polera_level_up),
    Producto("Pc Gamer", 1899990, imageRes = R.drawable.pc_gamer_asus_strix),
    Producto("PlayStation 5", 549990, imageRes = R.drawable.playstation5)
)


// --- Pantalla Principal ---
@Composable
fun InicioScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    var isLoggedIn by remember { mutableStateOf(false) }
    val username = "Frank"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            HeaderSection(
                isLoggedIn = isLoggedIn,
                username = username,
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("registro") },
                onCartClick = { navController.navigate("cart")}
            )
        }
        item { CarouselSection(slides = carouselSlides) }
        item { BestSellersSection(products = bestSellers, navController = navController) }
        item { TechSupportBanner(context = LocalContext.current) }
    }
}


// --- Componentes Renovados ---

@Composable
fun HeaderSection(
    isLoggedIn: Boolean,
    username: String,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (isLoggedIn) {
                Text("¡Hola de nuevo,", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(username, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            } else {
                Text("Bienvenido a", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Level-Up Store", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
        if (isLoggedIn) {
            IconButton(onClick = onCartClick) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )) {
                Text("Salir")
            }
        } else {
            OutlinedButton(onClick = onRegisterClick) {
                Text("Registrarse")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onLoginClick) {
                Text("Iniciar Sesión")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselSection(slides: List<CarouselSlide>) {
    val pagerState = rememberPagerState(pageCount = { slides.size })

    Column {
        Text(
            text = "Sobre Nosotros",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 12.dp
        ) { page ->
            val slide = slides[page]
            CarouselCard(slide)
        }
        Row(
            Modifier
                .height(30.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun CarouselCard(slide: CarouselSlide) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            if (slide.imageRes != null) {
                Image(
                    painter = painterResource(id = slide.imageRes),
                    contentDescription = slide.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    slide.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = slide.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = slide.subtitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BestSellersSection(products: List<Producto>, navController: NavController) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nuestros Productos Más Vendidos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { navController.navigate("productos")  }) {
                Text("Ver todos")
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
            TextButton(onClick = { navController.navigate("api_products") }) {
                Text("Ver productos de la API")
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product, onClick = { /* navController.navigate("product_detail/${product.name}") */ })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(160.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (product.imageRes != null) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        product.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$${product.price.formatPrice()}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun TechSupportBanner(context: Context) {
    val whatsappUrl = "https://api.whatsapp.com/send?phone=56984543683&text=¡Hola!"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(whatsappUrl) }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "¿Necesitas Ayuda?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    "Contacta a nuestro soporte técnico",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.whatsapp_logo),
                contentDescription = "Contactar por WhatsApp",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
