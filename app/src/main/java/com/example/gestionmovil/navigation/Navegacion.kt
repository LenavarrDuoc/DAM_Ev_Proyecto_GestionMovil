package com.example.gestionmovil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestionmovil.ui.theme.Login.LoginScreen

@Composable
fun AppNavegacion(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginSuccess = { usuario ->
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Rutas.Registro.ruta)
                }
            )
        }

        composable(Rutas.Registro.ruta) {
            // Pendiente: RegisterScreen()
        }

        composable(Rutas.Home.ruta) {
            // Pendiente: HomeScreen()
        }
    }
}