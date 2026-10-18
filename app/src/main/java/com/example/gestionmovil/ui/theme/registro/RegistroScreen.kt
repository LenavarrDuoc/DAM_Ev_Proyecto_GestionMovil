package com.example.gestionmovil.ui.theme.Registro

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestionmovil.ui.theme.registro.RegistroUiState
import java.util.Calendar
import java.util.Date

@Composable
fun RegistroScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RegistroContent(
        uiState = uiState,
        onPrimerNombreChange = viewModel::onPrimerNombreChange,
        onSegundoNombreChange = viewModel::onSegundoNombreChange,
        onPrimerApellidoChange = viewModel::onPrimerApellidoChange,
        onSegundoApellidoChange = viewModel::onSegundoApellidoChange,
        onFechaSelected = viewModel::onFechaNacimientoSelected,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onAceptaTerminosChange = viewModel::onAceptaTerminosChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onRegisterClick = { viewModel.registrarUsuario(onSuccess = onRegisterSuccess) },
        onNavigateToLogin = onNavigateToLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroContent(
    uiState: RegistroUiState,
    onPrimerNombreChange: (String) -> Unit,
    onSegundoNombreChange: (String) -> Unit,
    onPrimerApellidoChange: (String) -> Unit,
    onSegundoApellidoChange: (String) -> Unit,
    onFechaSelected: (Date) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAceptaTerminosChange: (Boolean) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            onFechaSelected(cal.time)
        },
        calendar.get(Calendar.YEAR) - 18,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al Login")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Nombres
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.primerNombre,
                        onValueChange = onPrimerNombreChange,
                        label = { Text("1er Nombre *") },
                        isError = uiState.errorPrimerNombre != null,
                        supportingText = {
                            uiState.errorPrimerNombre?.let { Text(it) }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = uiState.segundoNombre,
                        onValueChange = onSegundoNombreChange,
                        label = { Text("2do Nombre") },
                        isError = uiState.errorSegundoNombre != null,
                        supportingText = {
                            uiState.errorSegundoNombre?.let { Text(it) }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Apellidos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.primerApellido,
                        onValueChange = onPrimerApellidoChange,
                        label = { Text("1er Apellido *") },
                        isError = uiState.errorPrimerApellido != null,
                        supportingText = {
                            uiState.errorPrimerApellido?.let { Text(it) }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = uiState.segundoApellido,
                        onValueChange = onSegundoApellidoChange,
                        label = { Text("2do Apellido") },
                        isError = uiState.errorSegundoApellido != null,
                        supportingText = {
                            uiState.errorSegundoApellido?.let { Text(it) }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Fecha de Nacimiento
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.fechaNacimientoTexto,
                        onValueChange = {},
                        readOnly = true,
                        enabled = true,
                        label = { Text("Fecha Nacimiento *") },
                        isError = uiState.errorFechaNacimiento != null,
                        supportingText = {
                            uiState.errorFechaNacimiento?.let { Text(it) }
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { datePickerDialog.show() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Correo electrónico
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico *") },
                    isError = uiState.errorEmail != null,
                    supportingText = {
                        uiState.errorEmail?.let { Text(it) }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 5. Contraseña
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña *") },
                    isError = uiState.errorPassword != null,
                    supportingText = {
                        uiState.errorPassword?.let { Text(it) }
                    },
                    singleLine = true,
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(imageVector = icon, contentDescription = "Mostrar contraseña")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 6. Confirmar Contraseña
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text("Confirmar contraseña *") },
                    isError = uiState.errorConfirmPassword != null,
                    supportingText = {
                        uiState.errorConfirmPassword?.let { Text(it) }
                    },
                    singleLine = true,
                    visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val icon = if (uiState.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = onToggleConfirmPasswordVisibility) {
                            Icon(imageVector = icon, contentDescription = "Mostrar confirmación")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 7. Términos y Condiciones
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAceptaTerminosChange(!uiState.aceptaTerminos) }
                    ) {
                        Checkbox(
                            checked = uiState.aceptaTerminos,
                            onCheckedChange = onAceptaTerminosChange
                        )
                        Text(
                            text = "Acepto los términos y condiciones",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (uiState.errorTerminos != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    uiState.errorTerminos?.let { msg ->
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                // Mensaje global de Error
                uiState.errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registrarse
                Button(
                    onClick = onRegisterClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Registrarse")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    MaterialTheme {
        RegistroContent(
            uiState = RegistroUiState(
                primerNombre = "Carlos",
                primerApellido = "Gómez",
                fechaNacimientoTexto = "15/05/1995",
                email = "carlos@correo.com"
            ),
            onPrimerNombreChange = {},
            onSegundoNombreChange = {},
            onPrimerApellidoChange = {},
            onSegundoApellidoChange = {},
            onFechaSelected = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onAceptaTerminosChange = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onRegisterClick = {},
            onNavigateToLogin = {}
        )
    }
}