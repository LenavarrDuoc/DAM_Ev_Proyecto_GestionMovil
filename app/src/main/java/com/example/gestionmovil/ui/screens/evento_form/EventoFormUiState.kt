package com.example.gestionmovil.ui.screens.evento_form

import android.graphics.Bitmap

data class EventoFormUiState(
    // Campos del Formulario
    val titulo: String = "",
    val tipoEvento: String = "Conferencia",
    val fecha: String = "2026-10-25",
    val hora: String = "10:00",
    val region: String = "Región Metropolitana",
    val comuna: String = "Santiago",
    val organizacionNombre: String = "",
    val publicoEsperado: String = "",
    val condicionesParticipacion: String = "",
    val fotoEvidencia: Bitmap? = null,

    // Estados de UI y Validaciones
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val isEventoGuardado: Boolean = false,
    val errorMessage: String? = null,

    // Lista de tipos de eventos disponibles
    val tiposDisponibles: List<String> = listOf(
        "Conferencia",
        "Feria Comercial",
        "Reunión de Negocios",
        "Seminario",
        "Taller"
    )
)