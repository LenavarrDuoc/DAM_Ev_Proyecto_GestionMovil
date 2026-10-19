package com.example.gestionmovil.ui.seguimiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gestionmovil.data.local.entities.SeguimientoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    viewModel: SeguimientoViewModel,
    oportunidadId: Long,
    onVolver: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(oportunidadId) {
        viewModel.cargarSeguimientosPorOportunidad(oportunidadId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento Comercial") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    // BOTÓN ATRÁS
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // BOTÓN HOME
                    IconButton(onClick = onNavigateToHome) {
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
                onClick = {
                    viewModel.limpiarFormulario()
                    mostrarBottomSheet = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Gestión")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.listaSeguimientos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay gestiones registradas para esta oportunidad.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.listaSeguimientos) { seguimiento ->
                        ItemSeguimientoCard(
                            seguimiento = seguimiento,
                            onEditar = {
                                viewModel.seleccionarParaEditar(seguimiento)
                                mostrarBottomSheet = true
                            },
                            onEliminar = { viewModel.eliminarSeguimiento(seguimiento) }
                        )
                    }
                }
            }
        }
    }

    // Modal para Registrar / Editar Gestión
    if (mostrarBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                mostrarBottomSheet = false
                viewModel.limpiarFormulario()
            }
        ) {
            FormularioSeguimientoContent(
                uiState = uiState,
                viewModel = viewModel,
                onGuardar = {
                    viewModel.guardarGestion()
                    mostrarBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun ItemSeguimientoCard(
    seguimiento: SeguimientoEntity,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = seguimiento.tipoGestion,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = seguimiento.fechaGestion,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (seguimiento.propuesta.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Propuesta: ${seguimiento.propuesta}", style = MaterialTheme.typography.bodyMedium)
            }

            if (seguimiento.respuestaCliente.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Respuesta: ${seguimiento.respuestaCliente}", style = MaterialTheme.typography.bodyMedium)
            }

            if (seguimiento.proximaAccion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Próx. Acción: ${seguimiento.proximaAccion} (${seguimiento.fechaProximaAccion})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun FormularioSeguimientoContent(
    uiState: SeguimientoUiState,
    viewModel: SeguimientoViewModel,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = if (uiState.idSeleccionado == null) "Registrar Gestión" else "Editar Gestión",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = uiState.tipoGestion,
            onValueChange = { viewModel.onTipoGestionChanged(it) },
            label = { Text("Tipo de Gestión (Llamada, Correo...)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.propuesta,
            onValueChange = { viewModel.onPropuestaChanged(it) },
            label = { Text("Propuesta Presentada") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.respuestaCliente,
            onValueChange = { viewModel.onRespuestaClienteChanged(it) },
            label = { Text("Respuesta del Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.proximaAccion,
            onValueChange = { viewModel.onProximaAccionChanged(it) },
            label = { Text("Próxima Acción / Compromiso") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.fechaProximaAccion,
            onValueChange = { viewModel.onFechaProximaAccionChanged(it) },
            label = { Text("Fecha Próxima Acción (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onGuardar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Registro")
        }
    }
}

// --- PREVIEWS PARA ANDROID STUDIO ---

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Barra Superior (TopAppBar)")
@Composable
fun SeguimientoTopBarPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text("Seguimiento Comercial") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
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
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ir a Inicio"
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "Tarjeta de Seguimiento")
@Composable
fun ItemSeguimientoCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ItemSeguimientoCard(
                seguimiento = SeguimientoEntity(
                    id = 1L,
                    oportunidadId = 100L,
                    tipoGestion = "Llamada de Calificación",
                    fechaGestion = "2026-10-19",
                    propuesta = "Implementación del módulo CRM Móvil",
                    respuestaCliente = "Interesados en la propuesta, solicitaron ajustar alcance.",
                    proximaAccion = "Enviar propuesta revisada",
                    fechaProximaAccion = "2026-10-25"
                ),
                onEditar = {},
                onEliminar = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Estado Vacío")
@Composable
fun SeguimientoVacioPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay gestiones registradas para esta oportunidad.")
        }
    }
}