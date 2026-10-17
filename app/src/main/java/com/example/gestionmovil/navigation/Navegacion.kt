package com.example.gestionmovil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestionmovil.ui.theme.Login.LoginScreen
import com.example.gestionmovil.ui.theme.Registro.RegistroScreen

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
            RegistroScreen(
                onRegisterSuccess = {
                    // Tras un registro exitoso, elimina Registro de la pila y vuelve a Login
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    // Acción al presionar volver/atrás desde Registro
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.Home.ruta) {
            // Pendiente: HomeScreen()
        }
    }
}