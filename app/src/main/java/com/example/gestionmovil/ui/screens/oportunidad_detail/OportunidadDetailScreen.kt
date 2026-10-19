package com.example.gestionmovil.ui.screens.oportunidad_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.ui.screens.oportunidad_form.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadDetailScreen(
    oportunidadId: Long,
    viewModel: OportunidadDetailViewModel,
    usuarioActual: UsuarioEntity?,
    onNavigateBack: () -> Unit,
    onNavigateToSeguimiento: (Long) -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(oportunidadId) {
        viewModel.loadOportunidad(oportunidadId)
    }

    val esSupervisor = usuarioActual?.esSupervisor() == true
    val esEjecutivo = usuarioActual?.esEjecutivo() == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Oportunidad") },
                navigationIcon = {
                    // BOTÓN ATRÁS -> Vuelve a OportunidadListScreen
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // BOTÓN SEGUIMIENTO -> Va directo a la bitácora de gestiones
                    IconButton(onClick = { onNavigateToSeguimiento(oportunidadId) }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Ver Seguimiento"
                        )
                    }
                    // BOTÓN HOME -> Vuelve directo a InicioScreen
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Ir a Inicio"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            val op = uiState.oportunidad
            if (op == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No se encontró la oportunidad solicitada.")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tarjeta Principal
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = op.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = op.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Información Detallada
                    DetailItem(
                        icon = Icons.Default.BusinessCenter,
                        label = "Tipo",
                        value = op.tipoOportunidad
                    )
                    DetailItem(
                        icon = Icons.Default.PriorityHigh,
                        label = "Prioridad",
                        value = op.prioridad
                    )
                    DetailItem(
                        icon = Icons.Default.Person,
                        label = "Responsable",
                        value = op.responsableInterno
                    )
                    DetailItem(
                        icon = Icons.Default.CalendarToday,
                        label = "Fecha de Detección",
                        value = op.fechaDeteccion
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón para Ir a la Bitácora de Seguimiento
                    Button(
                        onClick = { onNavigateToSeguimiento(op.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Bitácora / Registro de Seguimiento")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cambio Dinámico de Estado
                    Text(
                        text = "Gestión de Estado de la Oportunidad",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    val opcionesEstado = if (esEjecutivo) {
                        listOf(
                            "Evento identificado",
                            "Contacto pendiente",
                            "Contacto realizado",
                            "Propuesta enviada",
                            "En espera de respuesta"
                        )
                    } else {
                        listOf(
                            "Evento identificado", "Contacto pendiente", "Contacto realizado",
                            "Propuesta enviada", "En espera de respuesta", "Participación confirmada",
                            "Oportunidad descartada"
                        )
                    }

                    DropdownSelector(
                        label = "Estado Actual",
                        selectedOption = op.estado,
                        options = opcionesEstado,
                        onOptionSelected = { newStatus ->
                            viewModel.updateEstado(newStatus, currentDate = "2026-10-19")
                        }
                    )

                    // Reasignación exclusiva para el Supervisor
                    if (esSupervisor) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Asignación de Ejecutivo Responsable",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        val ejecutivosDisponibles = listOf("Ana López", "Mateo Silva", "Sofía Rojas")

                        DropdownSelector(
                            label = "Ejecutivo Asignado",
                            selectedOption = op.responsableInterno,
                            options = ejecutivosDisponibles,
                            onOptionSelected = { nuevoEjecutivo ->
                                viewModel.reorganizarResponsable(op.id, nuevoEjecutivo)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// --- PREVIEWS PARA ANDROID STUDIO ---

@Preview(showBackground = true, name = "Ítem de Detalle")
@Composable
fun DetailItemPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DetailItem(
                icon = Icons.Default.BusinessCenter,
                label = "Tipo de Oportunidad",
                value = "Licitación Pública"
            )
        }
    }
}

@Preview(showBackground = true, name = "Tarjeta Principal Oportunidad")
@Composable
fun OportunidadCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Licencia CRM Corporativo 2026",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Implementación de licencias para el equipo comercial de la zona central.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Barra Superior (TopAppBar)")
@Composable
fun OportunidadDetailTopBarPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text("Detalle de Oportunidad") },
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
                        imageVector = Icons.Default.History,
                        contentDescription = "Ver Seguimiento"
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