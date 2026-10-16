package com.example.gestionmovil.ui.screens.contacto_form

data class ContactoFormUiState(
    val nombre: String = "",
    val nombreError: String? = null,

    val cargo: String = "",
    val cargoError: String? = null,

    val correo: String = "",
    val correoError: String? = null,

    val telefono: String = "",
    val telefonoError: String? = null,

    val medioPreferente: String = "",
    val medioPreferenteError: String? = null,
    val estado: String = "",
    val fecahUltimoContacto: String = "",

    val organizacionIdSeleccionada: Long? = null,
    val observacion: String = "",

    val isFormValid: Boolean = false,
    val isSavedSuccess: Boolean = false
)