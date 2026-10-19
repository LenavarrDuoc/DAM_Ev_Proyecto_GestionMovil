package com.example.gestionmovil.navigation

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.data.repository.SeguimientoRepository
import com.example.gestionmovil.ui.screens.InicioScreen
import com.example.gestionmovil.ui.seguimiento.SeguimientoScreen
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModel
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModelFactory
import com.example.gestionmovil.ui.screens.Login.LoginScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModel
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailScreen
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModel
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModelFactory
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormScreen
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModel
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModelFactory
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListScreen
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListViewModel
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListViewModelFactory
import com.example.gestionmovil.ui.theme.Registro.RegistroScreen

@Composable
fun AppNavegacion(
    navController: NavHostController = rememberNavController(),
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
            InicioScreen(navController = navController)
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

        composable(Rutas.OportunidadList.ruta) {
            // 1. Obtener la base de datos y el repositorio
            val database = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(database.oportunidadDao())

            // 2. Crear el ViewModel pasando su Factory explícita
            val oportunidadListViewModel: OportunidadListViewModel = viewModel(
                factory = OportunidadListViewModelFactory(repository)
            )

            // 3. Renderizar la pantalla
            OportunidadListScreen(
                navController = navController,
                viewModel = oportunidadListViewModel
            )
        }

        composable(
            route = Rutas.OportunidadDetail.ruta,
            arguments = listOf(
                navArgument("oportunidadId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            // 1. Extraer el id desde la ruta
            val oportunidadId = backStackEntry.arguments?.getLong("oportunidadId") ?: 0L

            // 2. Crear las dependencias
            val database = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(database.oportunidadDao())

            // 3. Crear el ViewModel con su Factory
            val detailViewModel: OportunidadDetailViewModel = viewModel(
                factory = OportunidadDetailViewModelFactory(repository)
            )

            // 4. Invocar la pantalla con su firma exacta (ella misma dispara el LaunchedEffect)
            OportunidadDetailScreen(
                oportunidadId = oportunidadId,
                viewModel = detailViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- FORMULARIO DE OPORTUNIDAD ---
        composable(Rutas.OportunidadForm.ruta) {
            val database = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(database.oportunidadDao())

            val formViewModel: OportunidadFormViewModel = viewModel(
                factory = OportunidadFormViewModelFactory(repository)
            )

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

        composable(Rutas.Alertas.ruta) {
            // Reemplaza por tu pantalla real de Alertas cuando la crees
            PlaceholderScreen(titulo = "Alertas y Notificaciones", navController = navController)
        }

        composable(Rutas.Eventos.ruta) {
            // Reemplaza por tu pantalla real de Eventos o Calendario
            PlaceholderScreen(titulo = "Calendario y Eventos", navController = navController)
        }

        composable(Rutas.Historial.ruta) {
            // Reemplaza por tu pantalla real de Historial
            PlaceholderScreen(titulo = "Historial de Actividades", navController = navController)
        }
    }
}

@Composable
fun PlaceholderScreen(titulo: String, navController: NavHostController) {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = titulo,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}