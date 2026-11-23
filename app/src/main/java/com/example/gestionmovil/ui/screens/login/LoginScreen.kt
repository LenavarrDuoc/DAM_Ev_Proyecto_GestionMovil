package com.example.gestionmovil.ui.screens.login

import com.example.gestionmovil.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.ui.theme.AppTheme
import com.example.gestionmovil.ui.theme.GestionMovilGreen
import com.example.gestionmovil.ui.theme.GestionMovilPurple
import com.example.gestionmovil.ui.theme.GestionMovilTheme

@Composable
fun LoginScreen(
    onLoginSuccess: (UsuarioEntity) -> Unit,
    onNavigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    // Recolectamos el StateFlow reactivamente de la UI
    val uiState by viewModel.uiState.collectAsState()

    // Se ejecuta al cargar la pantalla o al regresar desde otra vista
    LaunchedEffect(Unit) {
        viewModel.limpiarEstadoCompleto()
    }

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onLoginClick = { viewModel.onLoginClick(onSuccess = onLoginSuccess) },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_gestion_movil),
                contentDescription = "Logo Gestión Móvil",
                modifier = Modifier.fillMaxWidth(0.8f).height(140.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "Le damos la bienvenida",
                style = MaterialTheme.typography.headlineLarge,
                color = AppTheme.colors.primaryButton
            )

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo Correo Electrónico
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.errorCorreo,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.textFieldBorderFocused,
                    unfocusedBorderColor = AppTheme.colors.textFieldBorderUnfocused,

                    focusedLabelColor = AppTheme.colors.textFieldLabelFocused,
                    unfocusedLabelColor = AppTheme.colors.textFieldLabelUnfocused,

                    cursorColor = AppTheme.colors.textFieldCursor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(imageVector = icon, contentDescription = "Mostrar contraseña")
                    }
                },
                isError = uiState.errorPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.textFieldBorderFocused,
                    unfocusedBorderColor = AppTheme.colors.textFieldBorderUnfocused,
                    focusedLabelColor = AppTheme.colors.textFieldLabelFocused,
                    unfocusedLabelColor = AppTheme.colors.textFieldLabelUnfocused,
                    cursorColor = AppTheme.colors.textFieldCursor
                )
            )

            // Feedback de Error
            uiState.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Iniciar Sesión / Carga con colores personalizados desde AppTheme
            Button(
                onClick = {
                    when {
                        uiState.errorCorreo -> emailFocusRequester.requestFocus()
                        uiState.errorPassword -> passwordFocusRequester.requestFocus()
                    }
                    onLoginClick()
                },
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primaryButton,
                    contentColor = AppTheme.colors.onPrimaryButton
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = AppTheme.colors.onPrimaryButton,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onNavigateToRegister
            ){
                Text("¿No tienes cuenta? Regístrate aquí")
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(color = Color(0xFFFCF9F1)),
                    contentAlignment = Alignment.Center

                )
                {
                    Image(
                        painter = painterResource(R.drawable.vmc_duoc),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(color = Color(0xFFF0FAFD)),
                    contentAlignment = Alignment.Center
                )
                {
                    Image(
                        painter = painterResource(R.drawable.logo_duocuc),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginSreenPreview() {
    GestionMovilTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "usuario@correo.com",
                password = "123"
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}