package com.example.gestionmovil.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.repository.SeguimientoRepository
import com.example.gestionmovil.ui.seguimiento.SeguimientoScreen
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModel
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModelFactory
import com.example.gestionmovil.ui.screens.Login.LoginScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModel
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormScreen
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModel
import com.example.gestionmovil.ui.theme.Registro.RegistroScreen

@Composable
fun AppNavegacion(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

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

        // --- MÓDULO SEGUIMIENTO ---
        composable(
            route = Rutas.Seguimiento.ruta,
            arguments = listOf(
                navArgument("oportunidadId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            // 1. Obtener el ID de la oportunidad desde los argumentos de la ruta
            val oportunidadId = backStackEntry.arguments?.getLong("oportunidadId") ?: 0L

            // 2. Preparar dependencias del ViewModel
            val app = context.applicationContext as Application
            val database = AppDatabase.getDatabase(app) // Ajustar al singleton de tu DB
            val repository = SeguimientoRepository(database.seguimientoDao())

            // 3. Crear el ViewModel mediante su Factory
            val seguimientoViewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(app, repository)
            )

            // 4. Renderizar la pantalla
            SeguimientoScreen(
                viewModel = seguimientoViewModel,
                oportunidadId = oportunidadId,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // --- FORMULARIO DE OPORTUNIDAD ---
        composable(Rutas.OportunidadForm.ruta) {
            val formViewModel: OportunidadFormViewModel = viewModel()

            OportunidadFormScreen(
                viewModel = formViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- FORMULARIO DE CONTACTO ---
        composable(Rutas.ContactoForm.ruta) {
            val contactoViewModel: ContactoFormViewModel = viewModel()

            ContactoFormScreen(
                viewModel = contactoViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}