package com.example.gestionmovil.data.remote.dto

// Equivale a UsuarioInputDTO
data class UsuarioRequestDto(
    val pNombre: String,
    val sNombre: String,
    val pApellido: String,
    val sApellido: String,
    val fecNac: Long,
    val email: String,
    val password: String, // Solo viaja al crear
    val rol: String,
    val fotoUri: String
)