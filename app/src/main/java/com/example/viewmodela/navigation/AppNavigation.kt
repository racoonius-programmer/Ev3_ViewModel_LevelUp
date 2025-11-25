package com.example.viewmodela.navigation

import ResumenScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viewmodela.R
import com.example.viewmodela.model.Producto
import com.example.viewmodela.ui.ProductScreen
import com.example.viewmodela.ui.screen.RegistroScreen
import com.example.viewmodela.ui.screen.InicioScreen
import com.example.viewmodela.viewmodel.UsuarioViewModel
import com.example.viewmodela.ui.screen.AddProductScreen
import com.example.viewmodela.ui.screen.CartScreen
import com.example.viewmodela.ui.screen.LoginScreen
import com.example.viewmodela.ui.screen.ProductListScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    //Aquí creamos el ViewModel una vez
    val usuarioViewModel: UsuarioViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = "inicio"
    ){
        composable("inicio"){
            // SOLUCIÓN: Pasa el viewModel a la pantalla de inicio.
            InicioScreen(navController, usuarioViewModel)
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
        composable("api_products") {
            ProductScreen()
        }
    }
}
