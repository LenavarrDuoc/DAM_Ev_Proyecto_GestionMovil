package com.example.gestionmovil.ui.theme.Registro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.repository.UsuarioRepository
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.ui.screens.registro.RegistroUiState
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

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    init {
        val dao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(dao)
    }

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // ─── ACTUALIZACIÓN DE CAMPOS ─────────────────────────────────

    fun onPrimerNombreChange(valor: String) {
        _uiState.update { it.copy(primerNombre = valor, errorPrimerNombre = null, errorMessage = null) }
    }

    fun onSegundoNombreChange(valor: String) {
        _uiState.update { it.copy(segundoNombre = valor, errorSegundoNombre = null, errorMessage = null) }
    }

    fun onPrimerApellidoChange(valor: String) {
        _uiState.update { it.copy(primerApellido = valor, errorPrimerApellido = null, errorMessage = null) }
    }

    fun onSegundoApellidoChange(valor: String) {
        _uiState.update { it.copy(segundoApellido = valor, errorSegundoApellido = null, errorMessage = null) }
    }

    fun onFechaNacimientoSelected(date: Date) {
        val fechaFormateada = dateFormatter.format(date)
        _uiState.update {
            it.copy(
                fechaNacimientoTexto = fechaFormateada,
                fechaNacimientoDate = date,
                errorFechaNacimiento = null,
                errorMessage = null
            )
        }
    }

    fun onEmailChange(valor: String) {
        _uiState.update { it.copy(email = valor, errorEmail = null, errorMessage = null) }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update { it.copy(password = valor, errorPassword = null, errorMessage = null) }
    }

    fun onConfirmPasswordChange(valor: String) {
        _uiState.update { it.copy(confirmPassword = valor, errorConfirmPassword = null, errorMessage = null) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta, errorTerminos = null, errorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    // ─── REGISTRO DE USUARIO ──────────────────────────────────────

    fun registrarUsuario(onSuccess: () -> Unit) {
        val estado = _uiState.value

        // Ejecutar validaciones aisladas por campo
        val errPNombre = ValidacionesRegistro.validarPrimerNombre(estado.primerNombre)
        val errSNombre = ValidacionesRegistro.validarSegundoNombre(estado.segundoNombre)
        val errPApellido = ValidacionesRegistro.validarPrimerApellido(estado.primerApellido)
        val errSApellido = ValidacionesRegistro.validarSegundoApellido(estado.segundoApellido)
        val errFecha = ValidacionesRegistro.validarFechaNacimiento(estado.fechaNacimientoDate)
        val errEmail = ValidacionesRegistro.validarEmail(estado.email)
        val errPass = ValidacionesRegistro.validarPassword(estado.password)
        val errConfirm = ValidacionesRegistro.validarConfirmPassword(estado.password, estado.confirmPassword)
        val errTerminos = ValidacionesRegistro.validarTerminos(estado.aceptaTerminos)

        val hayErrores = listOfNotNull(
            errPNombre, errSNombre, errPApellido, errSApellido,
            errFecha, errEmail, errPass, errConfirm, errTerminos
        ).isNotEmpty()

        _uiState.update {
            it.copy(
                errorPrimerNombre = errPNombre,
                errorSegundoNombre = errSNombre,
                errorPrimerApellido = errPApellido,
                errorSegundoApellido = errSApellido,
                errorFechaNacimiento = errFecha,
                errorEmail = errEmail,
                errorPassword = errPass,
                errorConfirmPassword = errConfirm,
                errorTerminos = errTerminos,
                errorMessage = if (hayErrores) "Por favor corrige los errores señalados en el formulario" else null
            )
        }

        if (hayErrores) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val nuevoUsuarioEntity = UsuarioEntity(
                id = UUID.randomUUID().toString(),
                pNombre = estado.primerNombre.trim(),
                sNombre = estado.segundoNombre.trim().ifEmpty { null },
                pApellido = estado.primerApellido.trim(),
                sApellido = estado.segundoApellido.trim().ifEmpty { null },
                fecNac = estado.fechaNacimientoDate!!.time,
                email = estado.email.trim(),
                password = estado.password
            )

            val resultado = repository.registrarUsuario(nuevoUsuarioEntity)

            _uiState.update { it.copy(isLoading = false) }

            resultado.onSuccess {
                _uiState.value = RegistroUiState()
                onSuccess()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al guardar el usuario en la base de datos")
                }
            }
        }
    }
}