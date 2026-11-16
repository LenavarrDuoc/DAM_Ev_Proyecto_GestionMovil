package com.example.gestionmovil.data.remote.dto

data class OportunidadResponseDto(
    val id: Long,
    val nombre: String,
    val tipoOportunidad: String,
    val descripcion: String,
    val fuenteInformacion: String,
    val fechaDeteccion: String,
    val responsableInterno: String,
    val prioridad: String,
    val potencialEstimado: String,
    val estado: String,
    val fechaUltimaActualizacion: String,
    val organizacionId: Long? = null,
    val observacion: String? = null
)