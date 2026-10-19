package com.example.gestionmovil.ui.screens.evento_form

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoFormScreen(
    viewModel: EventoFormViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var expandedTipo by remember { mutableStateOf(false) }

    // Recurso 1: Cámara Nativa
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.onFotoCapturada(bitmap)
            Toast.makeText(context, "Evidencia fotográfica capturada", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Evento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Título
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                label = { Text("Título / Nombre del Evento *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Tipo de Evento Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = uiState.tipoEvento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Evento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    uiState.tiposDisponibles.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                viewModel.onTipoEventoChange(tipo)
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            // Fecha y Horario
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.fecha,
                    onValueChange = { viewModel.onFechaChange(it) },
                    label = { Text("Fecha (AAAA-MM-DD)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.hora,
                    onValueChange = { viewModel.onHoraChange(it) },
                    label = { Text("Horario (HH:mm)") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Región y Comuna
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.region,
                    onValueChange = { viewModel.onRegionChange(it) },
                    label = { Text("Región") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.comuna,
                    onValueChange = { viewModel.onComunaChange(it) },
                    label = { Text("Comuna") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Organización
            OutlinedTextField(
                value = uiState.organizacionNombre,
                onValueChange = { viewModel.onOrganizacionChange(it) },
                label = { Text("Organización Asociada *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Público Esperado
            OutlinedTextField(
                value = uiState.publicoEsperado,
                onValueChange = { viewModel.onPublicoEsperadoChange(it) },
                label = { Text("Público Esperado (N° Asistentes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Condiciones
            OutlinedTextField(
                value = uiState.condicionesParticipacion,
                onValueChange = { viewModel.onCondicionesChange(it) },
                label = { Text("Condiciones de Participación") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            HorizontalDivider()

            // SECCIÓN FOTO (CÁMARA NATIVA)
            Text(
                text = "Foto de Evidencia (Obligatorio)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedButton(
                onClick = { cameraLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (uiState.fotoEvidencia == null) "Tomar Foto con Cámara Nativa" else "Volver a tomar Foto")
            }

            AnimatedVisibility(
                visible = uiState.fotoEvidencia != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.fotoEvidencia?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Evidencia Fotográfica",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // RECURSO 2: GUARDAR Y CALENDARIO NATIVO
            Button(
                onClick = {
                    // Pasamos `context` como primer argumento para permitir guardar la foto en disco
                    viewModel.guardarEvento(context) { evento ->
                        agendarEnCalendarioNativo(
                            context = context,
                            titulo = "${evento.titulo} (${evento.tipoEvento})",
                            descripcion = "Organización: ${evento.organizacionNombre}\nUbicación: ${evento.comuna},${evento.region}\nPúblico: ${evento.publicoEsperado}\nCondiciones:${evento.condicionesParticipacion}"
                        )
                        Toast.makeText(context, "Evento registrado y agendado", Toast.LENGTH_LONG).show()
                        onNavigateBack()
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Event, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar y Agendar en Calendario Nativo")
                }
            }
        }
    }
}

private fun agendarEnCalendarioNativo(
    context: Context,
    titulo: String,
    descripcion: String
) {
    val calIntent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, titulo)
        putExtra(CalendarContract.Events.DESCRIPTION, descripcion)

        val beginTime = Calendar.getInstance()
        val endTime = Calendar.getInstance().apply { add(Calendar.HOUR, 2) }

        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
    }

    try {
        context.startActivity(calIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se encontró aplicación de calendario", Toast.LENGTH_SHORT).show()
    }
}