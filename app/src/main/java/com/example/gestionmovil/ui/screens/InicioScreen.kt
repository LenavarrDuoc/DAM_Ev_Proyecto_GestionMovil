package com.example.gestionmovil.ui.screens

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gestionmovil.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavController,
    userRole: String,
    userName: String = "Usuario", // Parámetro para recibir el nombre del usuario
    cantidadOportunidades: Int = 0,
    onLogout: () -> Unit = {}
) {
    // Extrae únicamente el primer nombre (ej: "Ana María" -> "Ana")
    val primerNombre = userName.trim().split("\\s+".toRegex()).firstOrNull()?.takeIf { it.isNotBlank() } ?: "Usuario"

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

                    // Círculo con Fotografía de Perfil
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Foto de perfil",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
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