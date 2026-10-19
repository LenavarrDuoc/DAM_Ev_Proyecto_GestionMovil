package com.example.gestionmovil.ui.screens.evento_detail

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gestionmovil.ui.screens.evento_form.EventoFormViewModel
import com.example.gestionmovil.utils.ImageUtils
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetailScreen(
    eventoId: String,
    viewModel: EventoFormViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val eventosList by viewModel.eventosList.collectAsState()

    val evento = eventosList.find { it.id == eventoId }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (evento == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Cargar el bitmap guardado en el disco local usando su ruta de Room
            val fotoBitmap = remember(evento.rutaFotoEvidencia) {
                ImageUtils.cargarBitmapDesdeRuta(evento.rutaFotoEvidencia)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título y Tipo de Evento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = evento.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    AssistChip(
                        onClick = {},
                        label = { Text(evento.tipoEvento) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }

                HorizontalDivider()

                // MUESTRA VISUAL DE LA FOTO CAPTURADA
                Text(
                    text = "Evidencia Fotográfica",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (fotoBitmap != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            bitmap = fotoBitmap.asImageBitmap(),
                            contentDescription = "Foto de evidencia del evento",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (!evento.rutaFotoEvidencia.isNullOrEmpty()) {
                                    "Foto guardada en almacenamiento local"
                                } else {
                                    "Sin evidencia fotográfica capturada"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Tarjeta con información detallada
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailItem(
                            icon = Icons.Default.Event,
                            label = "Fecha y Hora",
                            value = "${evento.fecha} a las ${evento.hora} hrs"
                        )

                        DetailItem(
                            icon = Icons.Default.LocationOn,
                            label = "Ubicación",
                            value = "${evento.comuna}, ${evento.region}"
                        )

                        DetailItem(
                            icon = Icons.Default.Business,
                            label = "Organización",
                            value = evento.organizacionNombre
                        )

                        DetailItem(
                            icon = Icons.Default.Group,
                            label = "Público Esperado",
                            value = "${evento.publicoEsperado} asistentes"
                        )

                        DetailItem(
                            icon = Icons.Default.Info,
                            label = "Condiciones de Participación",
                            value = if (evento.condicionesParticipacion.isBlank()) "Sin condiciones especificadas" else evento.condicionesParticipacion
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Re-exportar al Calendario Nativo (Recurso 2)
                Button(
                    onClick = {
                        agendarEnCalendarioNativo(
                            context = context,
                            titulo = "${evento.titulo} (${evento.tipoEvento})",
                            descripcion = "Organización: ${evento.organizacionNombre}\nUbicación: ${evento.comuna}, ${evento.region}\nPúblico: ${evento.publicoEsperado}\nCondiciones: ${evento.condicionesParticipacion}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Event, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Re-agendar en Calendario Nativo")
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
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