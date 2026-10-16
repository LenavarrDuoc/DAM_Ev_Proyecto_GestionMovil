package com.example.gestionmovil.ui.theme.Login

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.AppDatabase
import com.example.gestionmovil.data.UsuarioRepository
import com.example.gestionmovil.models.Usuario
import com.example.gestionmovil.utils.Validaciones
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    init {
        val dao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(dao)
    }

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(nuevoEmail: String) {
        email = nuevoEmail
        errorMessage = null
    }

    fun onPasswordChange(nuevaPassword: String) {
        password = nuevaPassword
        errorMessage = null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onLoginClick(onSuccess: (Usuario) -> Unit) {
        // 1. Validar campos con Validaciones.kt
        val errorFormulario = Validaciones.validarLogin(email, password)
        if (errorFormulario != null) {
            errorMessage = errorFormulario
            return
        }

        // 2. Consultar la BD local
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val resultado = repository.loginUsuario(email, password)

            isLoading = false
            resultado.onSuccess { usuario ->
                onSuccess(usuario)
            }.onFailure { error ->
                errorMessage = error.message ?: "Error al iniciar sesión"
            }
        }
    }
}