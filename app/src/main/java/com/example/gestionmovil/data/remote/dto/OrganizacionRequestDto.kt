package com.example.gestionmovil.data.remote.dto

// Equivale a OrganizacionInputDTO
data class OrganizacionRequestDto(
    val nombre: String,
    val tipoOrganizacion: String,
    val rubro: String,
    val comuna: String,
    val region: String,
    val website: String = "",
    val audienciaObjetivo: String = "",
    val observacion: String = ""
)