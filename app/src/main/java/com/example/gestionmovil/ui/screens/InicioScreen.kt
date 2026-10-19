package com.example.gestionmovil.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestionmovil.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavController,
    userRole: String = "Supervisor" // O el rol dinámico obtenido del StateFlow
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard Principal") },
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
            // Tarjeta de Bienvenida
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bienvenido de nuevo",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Rol activo: $userRole",
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                    value = "12",
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
                onClick = { navController.navigate(Rutas.Eventos.ruta) },
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