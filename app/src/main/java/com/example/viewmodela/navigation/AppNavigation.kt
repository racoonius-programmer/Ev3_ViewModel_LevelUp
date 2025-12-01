package com.example.viewmodela.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viewmodela.MyApplication
import com.example.viewmodela.R
import com.example.viewmodela.model.Producto
import com.example.viewmodela.ui.ProductViewModel
import com.example.viewmodela.ui.screen.RegistroScreen
import com.example.viewmodela.ui.screen.InicioScreen
import com.example.viewmodela.viewmodel.UsuarioViewModel
import com.example.viewmodela.ui.screen.AddProductScreen
import com.example.viewmodela.ui.screen.CartScreen
import com.example.viewmodela.ui.screen.LoginScreen
import com.example.viewmodela.ui.screen.ProductDetailScreen
import com.example.viewmodela.ui.screen.ProductListScreen
import com.example.viewmodela.ui.screen.ResumenScreen
import com.example.viewmodela.ui.screen.ApiProductListScreen
import com.example.viewmodela.ui.screen.LocalProductListScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication
    val database = application.database

    // ViewModels
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModel.Factory(database.productoDao())
    )

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ){
        composable("inicio"){
            InicioScreen(navController, usuarioViewModel, productViewModel)
        }
        composable("registro"){
            RegistroScreen(navController, usuarioViewModel)
        }
        composable("login"){
            LoginScreen(
                navController = navController,
                onLogin = {
                navController.navigate("inicio") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("resumen"){
            ResumenScreen(usuarioViewModel)
        }
        composable("productos") {
            ProductListScreen(
                onGoToCart = { navController.navigate("cart") },
                onAddProduct = { navController.navigate("add") }
            )
        }

        composable("add") {
            AddProductScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("cart") {
            //Todo es estatico por ahora
            val demoCart = listOf(
                Producto("Polera Level Up", 15990, imageRes = R.drawable.polera_level_up),
                Producto("Pc Gamer", 1899990, imageRes = R.drawable.pc_gamer_asus_strix),
                Producto("PlayStation 5", 549990, imageRes = R.drawable.playstation5)
            )
            CartScreen(cart = demoCart, onBack = { navController.popBackStack() })
        }
        
        // Lista de productos desde API (Solo aquí se puede guardar)
        composable("api_products") {
            ApiProductListScreen(vm = productViewModel, navController = navController)
        }
        
        // Lista de productos LOCAL (Solo aquí se puede borrar)
        composable("local_products") {
            LocalProductListScreen(vm = productViewModel, navController = navController)
        }
        
        // Detalle de producto
        composable("product_detail") {
            ProductDetailScreen(navController = navController)
        }
    }
}
