package com.example.gestionmovil.ui.screens.perfil

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gestionmovil.utils.CamaraUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFotoScreen(
    fotoActualUri: String?,
    onFotoGuardada: (String?) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit = {}
) {
    val context = LocalContext.current
    var imagenUri by remember { mutableStateOf<Uri?>(fotoActualUri?.let { Uri.parse(it) }) }
    var hayFoto by remember { mutableStateOf(imagenUri != null) }

    // Sincroniza estado cuando cambia la uri
    LaunchedEffect(imagenUri) {
        if (imagenUri != null) {
            hayFoto = true
        }
    }

    val scope = rememberCoroutineScope()
    val timeEffect = 600
    val timeEffect2 = 400

    // Launcher para Galería
    val selectorFotos = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imagenUri = uri
            hayFoto = true
        }
    }

    // Launcher para Cámara (idéntico al de FotoMotion)
    var uriPendiente by rememberSaveable { mutableStateOf<Uri?>(null) }
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && uriPendiente != null) {
            imagenUri = uriPendiente
            hayFoto = true
        } else {
            uriPendiente = null
        }
    }

    val colorBoton1 by animateColorAsState(
        targetValue = if (hayFoto) Color(0xFF00897B) else Color(0xFF6750A4),
        animationSpec = tween(durationMillis = 600),
        label = "ColorBoton1"
    )

    val colorBoton2 by animateColorAsState(
        targetValue = if (hayFoto) Color(0xFFEE7777) else Color(0xFFADA4A4),
        animationSpec = tween(durationMillis = 600),
        label = "ColorBoton2"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotografía de Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Ir a Inicio"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium
            )

            Crossfade(
                targetState = hayFoto,
                label = "Estado de fotografía"
            ) { tieneFoto ->
                Text(
                    text = if (tieneFoto) "¡Fotografía seleccionada!" else "Selecciona una fotografía para comenzar"
                )
            }

            // Botón Selección Galería
            Button(
                onClick = { selectorFotos.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton1),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Fotografía")
            }

            // Botón Tomar Fotografía (Cámara)
            Button(
                onClick = {
                    try {
                        val nuevaUri = CamaraUtils.creaUriFotografia(context)
                        uriPendiente = nuevaUri
                        camaraLauncher.launch(nuevaUri)
                    } catch (e: Exception) {
                        uriPendiente = null
                        e.printStackTrace()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Fotografía.")
            }

            // Muestra de imagen con animación
            AnimatedVisibility(
                visible = hayFoto,
                enter = fadeIn(tween(timeEffect)) + expandVertically(tween(timeEffect)),
                exit = fadeOut(tween(timeEffect)) + shrinkVertically(tween(timeEffect))
            ) {
                AnimatedContent(
                    targetState = imagenUri,
                    transitionSpec = {
                        fadeIn(tween(timeEffect2)) togetherWith fadeOut(tween(timeEffect2))
                    },
                    label = "Transición imagen"
                ) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Fotografía seleccionada.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Botón de Guardar + Botón de Borrar
            AnimatedVisibility(
                visible = hayFoto,
                enter = fadeIn(tween(timeEffect)) + expandVertically(tween(timeEffect)),
                exit = fadeOut(tween(timeEffect)) + shrinkVertically(tween(timeEffect))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón para Confirmar y Guardar la Fotografía elegida
                    Button(
                        onClick = {
                            onFotoGuardada(imagenUri?.toString())
                            onNavigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar fotografía")
                    }

                    // Botón para Borrar la Fotografía
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                hayFoto = false
                                delay(timeEffect.milliseconds)
                                if (!hayFoto) {
                                    imagenUri = null
                                    onFotoGuardada(null)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorBoton2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Borrar fotografía.")
                    }
                }
            }
        }
    }
}