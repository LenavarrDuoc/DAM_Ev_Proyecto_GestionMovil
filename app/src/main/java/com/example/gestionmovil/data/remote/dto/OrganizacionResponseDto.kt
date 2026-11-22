package com.example.gestionmovil.data.remote.dto

// Equivale a OrganizacionResponseDTO
data class OrganizacionResponseDto(
    val id: Long,
    val nombre: String,
    val tipoOrganizacion: String,
    val rubro: String,
    val comuna: String,
    val region: String,
    val website: String?,
    val audienciaObjetivo: String?,
    val observacion: String?
)
