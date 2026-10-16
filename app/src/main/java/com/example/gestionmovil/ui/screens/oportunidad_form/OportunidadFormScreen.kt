package com.example.gestionmovil.ui.screens.oportunidad_form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionmovil.ui.components.CustomOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadFormScreen(
    viewModel: OportunidadFormViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegar de regreso tras guardar con éxito
    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Oportunidad") },
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
                text = "Antecedentes de la Oportunidad",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Nombre de la oportunidad
            CustomOutlinedTextField(
                value = uiState.nombre,
                onValueChange = viewModel::onNombreChanged,
                label = "Nombre de la oportunidad *",
                errorMessage = uiState.nombreError,
                leadingIcon = Icons.Default.BusinessCenter
            )

            // Descripción
            CustomOutlinedTextField(
                value = uiState.descripcion,
                onValueChange = viewModel::onDescripcionChanged,
                label = "Descripción *",
                errorMessage = uiState.descripcionError,
                leadingIcon = Icons.Default.Description,
                singleLine = false
            )

            // Fuente de información
            CustomOutlinedTextField(
                value = uiState.fuenteInformacion,
                onValueChange = viewModel::onFuenteInformacionChanged,
                label = "Fuente de información *",
                errorMessage = uiState.fuenteInformacionError,
                leadingIcon = Icons.Default.Info
            )

            // Responsable interno
            CustomOutlinedTextField(
                value = uiState.responsableInterno,
                onValueChange = viewModel::onResponsableChanged,
                label = "Responsable interno *",
                errorMessage = uiState.responsableInternoError,
                leadingIcon = Icons.Default.Person
            )

            // Seleccionadores tipo Dropdown
            DropdownSelector(
                label = "Tipo de Oportunidad",
                selectedOption = uiState.tipoOportunidad,
                options = listOf(
                    "Evento corporativo", "Feria empresarial", "Actividad municipal",
                    "Jornada de beneficios", "Feria institucional", "Actividad de trabajadores",
                    "Activación comercial", "Otra instancia relevante"
                ),
                onOptionSelected = viewModel::onTipoOportunidadSelected
            )

            DropdownSelector(
                label = "Nivel de Prioridad",
                selectedOption = uiState.prioridad,
                options = listOf("Alta", "Media", "Baja"),
                onOptionSelected = viewModel::onPrioridadSelected
            )

            DropdownSelector(
                label = "Potencial Comercial Estimado",
                selectedOption = uiState.potencialEstimado,
                options = listOf("Alto", "Medio", "Bajo"),
                onOptionSelected = viewModel::onPotencialSelected
            )

            // Observaciones
            CustomOutlinedTextField(
                value = uiState.observacion,
                onValueChange = viewModel::onObservacionChanged,
                label = "Observaciones adicionales",
                leadingIcon = Icons.AutoMirrored.Filled.Notes,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de guardado reactivo
            Button(
                onClick = { viewModel.saveOpportunity(currentDate = "2026-10-16") },
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Oportunidad")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}