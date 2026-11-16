package com.example.gestionmovil.data.remote.dto

data class UsuarioResponseDto(
    val id: Long,
    val email: String,
    val primerNombre: String,
    val rol: String,
    val token: String? // Si utilizas autenticación JWT- Evaluar.
)
