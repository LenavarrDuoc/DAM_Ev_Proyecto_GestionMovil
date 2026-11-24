package com.example.gestionmovil.ui.screens.registro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.repository.UsuarioRepository
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.utils.ValidacionesRegistro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class RegistroViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: UsuarioRepository = UsuarioRepository(
        AppDatabase.getDatabase(application).usuarioDao(),
        RetrofitClient.usuarioApi
    )
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // ─── HANDLERS DE CAMBIOS ──────────────────────────────────────

    fun onPrimerNombreChange(value: String) {
        _uiState.update { it.copy(primerNombre = value, errorPrimerNombre = ValidacionesRegistro.validarPrimerNombre(value)) }
    }

    fun onSegundoNombreChange(value: String) {
        _uiState.update { it.copy(segundoNombre = value, errorSegundoNombre = ValidacionesRegistro.validarSegundoNombre(value)) }
    }

    fun onPrimerApellidoChange(value: String) {
        _uiState.update { it.copy(primerApellido = value, errorPrimerApellido = ValidacionesRegistro.validarPrimerApellido(value)) }
    }

    fun onSegundoApellidoChange(value: String) {
        _uiState.update { it.copy(segundoApellido = value, errorSegundoApellido = ValidacionesRegistro.validarSegundoApellido(value)) }
    }

    fun onFechaNacimientoSelected(date: Date?) {
        val dateString = if (date != null) {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        } else {
            ""
        }
        val err = ValidacionesRegistro.validarFechaNacimiento(date)
        _uiState.update { it.copy(fechaNacimientoTexto = dateString, fechaNacimientoDate = date, errorFechaNacimiento = err) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorEmail = ValidacionesRegistro.validarEmail(value)) }
    }

    fun onPasswordChange(value: String) {
        val errPass = ValidacionesRegistro.validarPassword(value)
        val errConf = ValidacionesRegistro.validarConfirmPassword(value, _uiState.value.confirmPassword)
        _uiState.update { it.copy(password = value, errorPassword = errPass, errorConfirmPassword = errConf) }
    }

    fun onConfirmPasswordChange(value: String) {
        val errConf = ValidacionesRegistro.validarConfirmPassword(_uiState.value.password, value)
        _uiState.update { it.copy(confirmPassword = value, errorConfirmPassword = errConf) }
    }

    fun onAceptaTerminosChange(value: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = value, errorTerminos = ValidacionesRegistro.validarTerminos(value)) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    // ─── LÓGICA DE REGISTRO ───────────────────────────────────────

    fun registrarUsuario(onSuccess: () -> Unit) {
        val state = _uiState.value

        val errPN = ValidacionesRegistro.validarPrimerNombre(state.primerNombre)
        val errPA = ValidacionesRegistro.validarPrimerApellido(state.primerApellido)
        val errFN = ValidacionesRegistro.validarFechaNacimiento(state.fechaNacimientoDate)
        val errEm = ValidacionesRegistro.validarEmail(state.email)
        val errPw = ValidacionesRegistro.validarPassword(state.password)
        val errCp = ValidacionesRegistro.validarConfirmPassword(state.password, state.confirmPassword)
        val errTe = ValidacionesRegistro.validarTerminos(state.aceptaTerminos)

        if (errPN != null || errPA != null || errFN != null || errEm != null || errPw != null || errCp != null || errTe != null) {
            _uiState.update {
                it.copy(
                    errorPrimerNombre = errPN,
                    errorPrimerApellido = errPA,
                    errorFechaNacimiento = errFN,
                    errorEmail = errEm,
                    errorPassword = errPw,
                    errorConfirmPassword = errCp,
                    errorTerminos = errTe,
                    errorMessage = "Por favor, corrige los errores en el formulario"
                )
            }
            return
        }

        val nuevoUsuario = UsuarioEntity(
            id = UUID.randomUUID().toString(),
            pNombre = state.primerNombre,
            sNombre = state.segundoNombre.ifBlank { null },
            pApellido = state.primerApellido,
            sApellido = state.segundoApellido.ifBlank { null },
            fecNac = state.fechaNacimientoDate?.time ?: 0L,
            email = state.email,
            password = state.password,
            rol = "CLIENTE",
            fotoUri = ""
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.registrarUsuario(nuevoUsuario)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Ocurrió un error al registrar el usuario"
                    )
                }
            }
        }
    }
}
