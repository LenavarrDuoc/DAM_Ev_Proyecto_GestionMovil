package com.example.gestionmovil.ui.screens.oportunidad_detail

import com.example.gestionmovil.data.local.entities.OportunidadEntity

data class OportunidadDetailUiState(
    val oportunidad: OportunidadEntity? = null,
    val isLoading: Boolean = false,
    val isStatusUpdated: Boolean = false,
    val isResponsableUpdated: Boolean = false,
    val errorMessage: String? = null
)