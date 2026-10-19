package com.example.gestionmovil.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage // Import de Coil 2
import com.example.gestionmovil.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavController,
    userRole: String,
    userName: String = "Usuario",
    fotoUri: String? = null, // <-- URI de la foto guardada en Room
    cantidadOportunidades: Int = 0,
    onNavigateToFotoPerfil: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val primerNombre = userName.trim().split("\\s+".toRegex()).firstOrNull()?.takeIf { it.isNotBlank() } ?: "Usuario"
    var mostrarImagenAmpliada by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión Móvil - Inicio") },
                actions = {
                    IconButton(onClick = { navController.navigate(Rutas.Alertas.ruta) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alertas")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Bienvenida Personalizada con Foto/Avatar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Te damos la bienvenida, $primerNombre",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rol activo: $userRole",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Círculo interactivo con Fotografía de Perfil o Icono por defecto
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable { mostrarImagenAmpliada = true },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (!fotoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = Uri.parse(fotoUri),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Foto de perfil por defecto",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Métrica Resumen (KPIs)
            Text(text = "Resumen de Gestión", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    title = "Oportunidades",
                    value = cantidadOportunidades.toString(),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Rutas.OportunidadList.ruta) }
                )
                KpiCard(
                    title = "Alertas",
                    value = "3",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Rutas.Alertas.ruta) }
                )
            }

            // Accesos Rápidos
            Text(text = "Módulos y Herramientas", style = MaterialTheme.typography.titleSmall)

            Button(
                onClick = { navController.navigate(Rutas.OportunidadList.ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ver Oportunidades")
            }

            OutlinedButton(
                onClick = { navController.navigate(Rutas.CalendarioEventos.ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Calendario / Eventos")
            }

            OutlinedButton(
                onClick = { navController.navigate(Rutas.Historial.ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Historial de Actividades")
            }

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }

    // Modal flotante con efecto Zoom Suave hacia el centro
    if (mostrarImagenAmpliada) {
        Dialog(onDismissRequest = { mostrarImagenAmpliada = false }) {
            AnimatedVisibility(
                visible = mostrarImagenAmpliada,
                enter = scaleIn(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .padding(8.dp)
                ) {
                    // Contenedor Circular Ampliado
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (!fotoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = Uri.parse(fotoUri),
                                    contentDescription = "Foto ampliada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Foto por defecto",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(140.dp)
                                )
                            }
                        }
                    }

                    // Botón para cerrar en la esquina superior derecha
                    IconButton(
                        onClick = { mostrarImagenAmpliada = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    // Ícono de Lápiz en la esquina inferior derecha
                    FloatingActionButton(
                        onClick = {
                            mostrarImagenAmpliada = false
                            onNavigateToFotoPerfil()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(48.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Foto",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(title: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            Text(text = title, style = MaterialTheme.typography.labelMedium)
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, name = "Inicio Screen Preview")
@Composable
fun InicioScreenPreview() {
    MaterialTheme {
        InicioScreen(
            navController = rememberNavController(),
            userRole = "Ejecutivo",
            userName = "Ana López Martínez"
        )
    }
}