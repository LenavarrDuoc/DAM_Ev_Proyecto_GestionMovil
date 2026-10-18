package com.example.gestionmovil.ui.screens.contacto_form

data class ContactoFormUiState(
    val organizacionIdSeleccionada: Long? = null,

    val nombreContacto: String = "",
    val nombreContactoError: String? = null,

    val cargo: String = "",
    val cargoError: String? = null,

    val area: String = "Eventos",

    val correo: String = "",
    val correoError: String? = null,

    val telefono: String = "",
    val telefonoError: String? = null,

    val medioPreferente: String = "Correo electrónico",
    val estado: String = "Activo",
    val observacion: String = "",

    val isFormValid: Boolean = false,
    val isSavedSuccess: Boolean = false
)