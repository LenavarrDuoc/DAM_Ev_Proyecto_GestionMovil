package com.example.gestionmovil.ui.screens.oportunidad_list

import com.example.gestionmovil.data.local.entities.OportunidadConOrganizacion

data class OportunidadListUiState(
    val searchQuery: String = "",
    val estadoFiltro: String? = null, // Ej: "En Negociación", "Ganada", "Perdida", null (Todos)
    val showFilterSheet: Boolean = false,
    val oportunidades: List<OportunidadConOrganizacion> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
