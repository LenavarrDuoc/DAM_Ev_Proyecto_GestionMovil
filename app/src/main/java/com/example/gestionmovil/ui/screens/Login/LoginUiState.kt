package com.example.gestionmovil.ui.screens.Login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorCorreo: Boolean = false,
    val errorPassword: Boolean = false,
    val errorMessage: String? = null
)