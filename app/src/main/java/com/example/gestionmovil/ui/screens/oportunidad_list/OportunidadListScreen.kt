package com.example.gestionmovil.ui.screens.oportunidad_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestionmovil.data.local.entities.OportunidadConOrganizacion
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadListScreen(
    navController: NavController,
    viewModel: OportunidadListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Oportunidades") },
                navigationIcon = {
                    // BOTÓN ATRÁS -> Regresa al menú / pantalla anterior
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // BOTÓN FILTRAR
                    IconButton(onClick = { viewModel.toggleFilterVisibility() }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar por estado"
                        )
                    }
                    // BOTÓN HOME -> Navega directo a InicioScreen
                    IconButton(onClick = {
                        navController.navigate(Rutas.Home.ruta) {
                            popUpTo(Rutas.Home.ruta) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Ir a Inicio"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Rutas.OportunidadForm.ruta) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Oportunidad"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Campo de Búsqueda por Título u Organización
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Buscar por título u organización...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                singleLine = true
            )

            // Panel de Filtros Desplegable con Animación
            AnimatedVisibility(
                visible = uiState.showFilterSheet,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Filtrar por Estado:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val opcionesEstado = listOf(
                            "Todos", "Evento identificado",
                            "Contacto pendiente", "Contacto realizado",
                            "Propuesta enviada", "En espera",
                            "Participación confirmada", "Oportunidad descartada"
                        )

                        opcionesEstado.forEach { estado ->
                            val isSelected = if (estado == "Todos") {
                                uiState.estadoFiltro == null
                            } else {
                                uiState.estadoFiltro.equals(estado, ignoreCase = true)
                            }

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.onEstadoFiltroSelected(if (estado == "Todos") null else estado)
                                },
                                label = { Text(estado) }
                            )
                        }
                    }
                }
            }

            // Indicador de Carga
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.oportunidades.isEmpty()) {
                // Estado Vacío / Sin Resultados
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron oportunidades",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Lista de Oportunidades Filtradas
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp, top = 4.dp)
                ) {
                    items(
                        items = uiState.oportunidades,
                        key = { it.oportunidad.id }
                    ) { item ->
                        OportunidadCard(
                            item = item,
                            onClick = {
                                navController.navigate(
                                    Rutas.OportunidadDetail.crearRuta(item.oportunidad.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OportunidadCard(
    item: OportunidadConOrganizacion,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.oportunidad.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.oportunidad.potencialEstimado,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Organización: ${item.organizacion?.nombre ?: "Sin Organización"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(
                    onClick = { },
                    label = { Text(item.oportunidad.estado) }
                )
                SuggestionChip(
                    onClick = { },
                    label = { Text("Prioridad: ${item.oportunidad.prioridad}") }
                )
            }
        }
    }
}

// --- PREVIEWS PARA ANDROID STUDIO ---

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Barra Superior (TopAppBar)")
@Composable
fun OportunidadListTopBarPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text("Listado de Oportunidades") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar por estado"
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ir a Inicio"
                    )
                }
            }
        )
    }
}

// --- PREVIEWS PARA ANDROID STUDIO ---


@Preview(showBackground = true, name = "Tarjeta Oportunidad")
@Composable
fun OportunidadCardItemPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Card(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Licitación Red de Fibra Óptica",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Medio",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Organización: Gobierno Regional",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Propuesta enviada") }
                        )
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Prioridad: Alta") }
                        )
                    }
                }
            }
        }
    }
}