package com.example.gestionmovil.ui.screens.contacto_form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gestionmovil.ui.components.CustomOutlinedTextField
import com.example.gestionmovil.ui.screens.oportunidad_form.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoFormScreen(
    viewModel: ContactoFormViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Contacto Corporativo") },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Datos del Contacto Ficticio",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            CustomOutlinedTextField(
                value = uiState.nombreContacto,
                onValueChange = viewModel::onNombreContactoChanged,
                label = "Nombre Completo *",
                errorMessage = uiState.nombreContactoError,
                leadingIcon = Icons.Default.Person
            )

            CustomOutlinedTextField(
                value = uiState.cargo,
                onValueChange = viewModel::onCargoChanged,
                label = "Cargo o Función *",
                errorMessage = uiState.cargoError,
                leadingIcon = Icons.Default.Work
            )

            DropdownSelector(
                label = "Área de Desempeño",
                selectedOption = uiState.area,
                options = listOf(
                    "Eventos", "Activaciones", "Marketing", "Comunicaciones",
                    "Recursos Humanos", "Bienestar", "Beneficios", "Administración"
                ),
                onOptionSelected = viewModel::onAreaSelected
            )

            CustomOutlinedTextField(
                value = uiState.correo,
                onValueChange = viewModel::onCorreoChanged,
                label = "Correo Electrónico Ficticio *",
                errorMessage = uiState.correoError,
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            CustomOutlinedTextField(
                value = uiState.telefono,
                onValueChange = viewModel::onTelefonoChanged,
                label = "Teléfono Ficticio *",
                errorMessage = uiState.telefonoError,
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            DropdownSelector(
                label = "Medio Preferente de Contacto",
                selectedOption = uiState.medioPreferente,
                options = listOf("Correo electrónico", "Llamada telefónica", "WhatsApp", "Reunión Presencial"),
                onOptionSelected = viewModel::onMedioPreferenteSelected
            )

            CustomOutlinedTextField(
                value = uiState.observacion,
                onValueChange = viewModel::onObservacionChanged,
                label = "Observaciones adicionales",
                leadingIcon = Icons.AutoMirrored.Filled.Notes,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveContacto(currentDate = "2026-10-16") },
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Contacto")
            }
        }
    }
}