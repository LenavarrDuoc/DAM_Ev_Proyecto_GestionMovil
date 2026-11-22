package com.example.gestionmovil.data.remote.dto

// Equivale a ContactoResponseDTO
data class ContactoResponseDto(
    val id: Long,
    val nombre: String,
    val organizacionId: Long?,
    val cargo: String,
    val area: String,
    val correo: String,
    val telefono: String,
    val medioPreferente: String,
    val estado: String,
    val fechaUltimoContacto: String,
    val observacion: String?
)
