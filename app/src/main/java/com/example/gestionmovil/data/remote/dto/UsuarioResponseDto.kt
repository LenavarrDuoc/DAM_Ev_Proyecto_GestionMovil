package com.example.gestionmovil.data.remote.dto

// Equivale a UsuarioResponseDTO
data class UsuarioResponseDto(
    val id: String, // Es String
    val pNombre: String?,
    val sNombre: String?,
    val pApellido: String?,
    val sApellido: String?,
    val fecNac: Long?,
    val email: String?,
    val rol: String?,
    val fotoUri: String?
)