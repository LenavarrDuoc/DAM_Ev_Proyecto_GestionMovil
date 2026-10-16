package com.example.gestionmovil.ui.screens.oportunidad_form

data class OportunidadFormUiState(
    val nombre: String = "",
    val nombreError: String? = null,

    val tipoOportunidad: String = "Evento corporativo",

    val descripcion: String = "",
    val descripcionError: String? = null,

    val fuenteInformacion: String = "",
    val fuenteInformacionError: String? = null,

    val responsableInterno: String = "",
    val responsableInternoError: String? = null,

    val prioridad: String = "Media",
    val potencialEstimado: String = "Medio",
    val estado: String = "Evento identificado",

    val organizacionIdSeleccionada: Long? = null,
    val observacion: String = "",

    val isFormValid: Boolean = false,
    val isSavedSuccess: Boolean = false
)