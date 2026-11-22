package com.example.gestionmovil.ui.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.repository.AuthRepository
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.utils.Validaciones
import com.example.gestionmovil.utils.Validaciones.esCorreoValido
import com.example.gestionmovil.utils.Validaciones.validarPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Cambiamos el tipo de repositorio a AuthRepository
    private val authRepository: AuthRepository

    init {
        // 2. Obtenemos las dependencias locales y remotas
        val dao = AppDatabase.getDatabase(application).usuarioDao()
        val authApi = RetrofitClient.authApi // ApiService de Retrofit

        // 3. Inyectamos ambas en el repositorio híbrido
        authRepository = AuthRepository(authApi, dao)
    }

    // Estado reactivo centralizado con StateFlow
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ─── HANDLERS DE CAMBIOS ──────────────────────────────────────

    fun onEmailChange(nuevoEmail: String) {
        _uiState.update {
            it.copy(email = nuevoEmail, errorCorreo = false, errorMessage = null)
        }
    }

    fun onPasswordChange(nuevaPassword: String) {
        _uiState.update {
            it.copy(password = nuevaPassword, errorPassword = false, errorMessage = null)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    // Limpia campos pero conserva el estado base
    fun limpiarCampos() {
        _uiState.update {
            it.copy(email = "", password = "", errorCorreo = false, errorPassword = false)
        }
    }

    // Reinicio total al recargar o regresar a la pantalla
    fun limpiarEstadoCompleto() {
        _uiState.value = LoginUiState()
    }

    // ─── AUTENTICACIÓN DE USUARIO ─────────────────────────────────

    fun onLoginClick(onSuccess: (UsuarioEntity) -> Unit) {
        val estado = _uiState.value

        // 1. Validar campos
        val errCorreo = !esCorreoValido(estado.email)
        val errPass = validarPassword(estado.password) != null
        val errorFormulario = Validaciones.validarLogin(estado.email, estado.password)

        _uiState.update {
            it.copy(
                errorCorreo = errCorreo,
                errorPassword = errPass,
                errorMessage = errorFormulario
            )
        }

        if (errorFormulario != null) return

        // 2. Consultar el repositorio híbrido (Retrofit + Room)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 3. Llamamos al método login que creamos en AuthRepository
            val resultado = authRepository.login(estado.email, estado.password)

            resultado.onSuccess { usuario ->
                limpiarEstadoCompleto()
                onSuccess(usuario)
            }.onFailure { error ->
                val msg = error.message ?: "Error al iniciar sesión"
                val esErrorCorreo = msg.contains("correo", ignoreCase = true)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        password = "", // Se limpia solo la contraseña para facilitar corregir
                        errorMessage = msg,
                        errorCorreo = esErrorCorreo,
                        errorPassword = !esErrorCorreo
                    )
                }
            }
        }
    }
}