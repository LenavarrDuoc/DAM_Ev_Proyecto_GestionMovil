package com.example.gestionmovil.data.remote.dto

// Equivale a UsuarioUpdateDTO
data class UsuarioUpdateDto(
    val pNombre: String,
    val sNombre: String,
    val pApellido: String,
    val sApellido: String,
    val fecNac: Long,
    val rol: String,
    val fotoUri: String
)
