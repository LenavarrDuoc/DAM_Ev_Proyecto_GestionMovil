package com.example.gestionmovil.navigation

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.repository.ContactoRepository
import com.example.gestionmovil.data.repository.EventoRepository
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.data.repository.SeguimientoRepository
import com.example.gestionmovil.ui.screens.InicioScreen
import com.example.gestionmovil.ui.screens.InicioViewModel
import com.example.gestionmovil.ui.screens.InicioViewModelFactory
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModel
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModelFactory
import com.example.gestionmovil.ui.screens.evento_detail.EventoDetailScreen
import com.example.gestionmovil.ui.screens.evento_form.EventoFormScreen
import com.example.gestionmovil.ui.screens.evento_form.EventoFormViewModel
import com.example.gestionmovil.ui.screens.evento_form.EventoFormViewModelFactory
import com.example.gestionmovil.ui.screens.evento_list.EventoListScreen
import com.example.gestionmovil.ui.screens.login.LoginScreen
import com.example.gestionmovil.ui.screens.login.LoginViewModel
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailScreen
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModel
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModelFactory
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormScreen
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModel
import com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModelFactory
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListScreen
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListViewModel
import com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListViewModelFactory
import com.example.gestionmovil.ui.screens.perfil.PerfilFotoScreen
import com.example.gestionmovil.ui.seguimiento.SeguimientoScreen
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModel
import com.example.gestionmovil.ui.seguimiento.SeguimientoViewModelFactory
import com.example.gestionmovil.ui.theme.Registro.RegistroScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estado que conserva la sesión del usuario en el grafo de navegación
    var usuarioActual by remember { mutableStateOf<UsuarioEntity?>(null) }

    val database = AppDatabase.getDatabase(context.applicationContext)

    // Repositorio e instancia de Eventos
    val eventoRepository = EventoRepository(database.eventoDao())
    val eventosViewModel: EventoFormViewModel = viewModel(
        factory = EventoFormViewModelFactory(eventoRepository)
    )

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        composable(Rutas.Login.ruta) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { usuario ->
                    usuarioActual = usuario
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
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // --- PANTALLA PRINCIPAL ADAPTADA SEGÚN EL ROL ---
        composable(Rutas.Home.ruta) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(db.oportunidadDao())
            val inicioViewModel: InicioViewModel = viewModel(
                factory = InicioViewModelFactory(repository)
            )
            val cantidadOportunidades by inicioViewModel.cantidadOportunidades.collectAsState(initial = 0)

            InicioScreen(
                navController = navController,
                userRole = usuarioActual?.rol ?: "Cliente",
                userName = usuarioActual?.pNombre ?: "Usuario",
                fotoUri = usuarioActual?.fotoUri, // <-- Foto guardada en Room
                cantidadOportunidades = cantidadOportunidades,
                onNavigateToFotoPerfil = {
                    navController.navigate(Rutas.PerfilFoto.ruta) // <-- Redirección desde el lápiz
                },
                onLogout = {
                    usuarioActual = null
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // --- MÓDULO GESTIÓN FOTO DE PERFIL ---
        composable(Rutas.PerfilFoto.ruta) {
            PerfilFotoScreen(
                fotoActualUri = usuarioActual?.fotoUri,
                onFotoGuardada = { nuevaUri ->
                    usuarioActual?.let { usuario ->
                        val usuarioActualizado = usuario.copy(fotoUri = nuevaUri)
                        usuarioActual = usuarioActualizado

                        // Persistencia asíncrona en Room
                        coroutineScope.launch {
                            database.usuarioDao().actualizarFotoPerfil(usuario.id, nuevaUri)
                        }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- MÓDULO SEGUIMIENTO ---
        composable(
            route = Rutas.Seguimiento.ruta,
            arguments = listOf(
                navArgument("oportunidadId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val oportunidadId = backStackEntry.arguments?.getLong("oportunidadId") ?: 0L

            val app = context.applicationContext as Application
            val db = AppDatabase.getDatabase(app)
            val repository = SeguimientoRepository(db.seguimientoDao())

            val seguimientoViewModel: SeguimientoViewModel = viewModel(
                factory = SeguimientoViewModelFactory(app, repository)
            )

            SeguimientoScreen(
                viewModel = seguimientoViewModel,
                oportunidadId = oportunidadId,
                onVolver = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

        // --- LISTADO DE OPORTUNIDADES ---
        composable(Rutas.OportunidadList.ruta) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(db.oportunidadDao())

            val oportunidadListViewModel: OportunidadListViewModel = viewModel(
                factory = OportunidadListViewModelFactory(repository)
            )

            OportunidadListScreen(
                navController = navController,
                viewModel = oportunidadListViewModel
            )
        }

        // --- DETALLE DE OPORTUNIDAD ---
        composable(
            route = Rutas.OportunidadDetail.ruta,
            arguments = listOf(navArgument("oportunidadId") { type = NavType.LongType })
        ) { backStackEntry ->
            val oportunidadId = backStackEntry.arguments?.getLong("oportunidadId") ?: 0L

            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(db.oportunidadDao())

            val detailViewModel: OportunidadDetailViewModel = viewModel(
                factory = OportunidadDetailViewModelFactory(repository)
            )

            OportunidadDetailScreen(
                oportunidadId = oportunidadId,
                viewModel = detailViewModel,
                usuarioActual = usuarioActual,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSeguimiento = { id ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(id))
                },
                onNavigateToHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

        // --- FORMULARIO DE OPORTUNIDAD ---
        composable(Rutas.OportunidadForm.ruta) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = OportunidadRepository(db.oportunidadDao())

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
            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = ContactoRepository(db.contactoDao())

            val contactoViewModel: ContactoFormViewModel = viewModel(
                factory = ContactoFormViewModelFactory(repository)
            )

            ContactoFormScreen(
                viewModel = contactoViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.Alertas.ruta) {
            PlaceholderScreen(titulo = "Alertas y Notificaciones", navController = navController)
        }

        composable(Rutas.CalendarioEventos.ruta) {
            EventoListScreen(
                viewModel = eventosViewModel,
                onNavigateToCrear = {
                    navController.navigate(Rutas.CrearEvento.ruta)
                },
                onNavigateToDetalle = { eventoId ->
                    navController.navigate(Rutas.DetalleEvento.crearRuta(eventoId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.CrearEvento.ruta) {
            EventoFormScreen(
                viewModel = eventosViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Rutas.DetalleEvento.ruta,
            arguments = listOf(navArgument("eventoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventoId = backStackEntry.arguments?.getString("eventoId") ?: ""
            EventoDetailScreen(
                eventoId = eventoId,
                viewModel = eventosViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.Historial.ruta) {
            PlaceholderScreen(titulo = "Historial de Actividades", navController = navController)
        }

        // --- MÓDULO GESTIÓN FOTO DE PERFIL ---
        composable(Rutas.PerfilFoto.ruta) {
            PerfilFotoScreen(
                fotoActualUri = usuarioActual?.fotoUri,
                onFotoGuardada = { nuevaUri ->
                    usuarioActual?.let { usuario ->
                        val usuarioActualizado = usuario.copy(fotoUri = nuevaUri)
                        usuarioActual = usuarioActualizado

                        // Persistencia asíncrona en Room
                        coroutineScope.launch {
                            database.usuarioDao().actualizarFotoPerfil(usuario.id, nuevaUri)
                        }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateHome = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(titulo: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Rutas.Home.ruta) {
                                popUpTo(Rutas.Home.ruta) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Ir a Inicio"
                        )
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
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}