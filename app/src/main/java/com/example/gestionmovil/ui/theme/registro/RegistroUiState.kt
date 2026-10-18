package com.example.gestionmovil.ui.theme.registro

import java.util.Date

data class RegistroUiState(
    val primerNombre: String = "",
    val segundoNombre: String = "",
    val primerApellido: String = "",
    val segundoApellido: String = "",
    val fechaNacimientoTexto: String = "",
    val fechaNacimientoDate: Date? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val aceptaTerminos: Boolean = false,
    // Visibilidad de contraseñas:
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val isLoading: Boolean = false,

    // Mensajes individuales para supportingText
    val errorPrimerNombre: String? = null,
    val errorSegundoNombre: String? = null,
    val errorPrimerApellido: String? = null,
    val errorSegundoApellido: String? = null,
    val errorFechaNacimiento: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val errorTerminos: String? = null,
    val errorMessage: String? = null
)
