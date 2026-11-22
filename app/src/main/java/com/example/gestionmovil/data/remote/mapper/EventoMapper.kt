package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.data.remote.dto.EventoRequestDto
import com.example.gestionmovil.data.remote.dto.EventoResponseDto
import com.example.gestionmovil.data.remote.dto.EventoUpdateDto

fun EventoResponseDto.toEntity(): EventoEntity {
    return EventoEntity(
        id = this.id,
        titulo = this.titulo ?: "",
        tipoEvento = this.tipoEvento ?: "",
        fecha = this.fecha ?: "",
        hora = this.hora ?: "",
        region = this.region ?: "",
        comuna = this.comuna ?: "",
        organizacionId = this.organizacionId ?: 0L,
        organizacionNombre = this.organizacionNombre ?: "",
        publicoEsperado = this.publicoEsperado ?: 0,
        condicionesParticipacion = this.condicionesParticipacion ?: "",
        rutaFotoEvidencia = this.rutaFotoEvidencia
    )
}

fun List<EventoResponseDto>.toEntityList(): List<EventoEntity> {
    return this.map { it.toEntity() }
}

fun EventoEntity.toRequestDto(): EventoRequestDto {
    return EventoRequestDto(
        titulo = this.titulo,
        tipoEvento = this.tipoEvento,
        fecha = this.fecha,
        hora = this.hora,
        region = this.region,
        comuna = this.comuna,
        organizacionId = this.organizacionId,
        organizacionNombre = this.organizacionNombre,
        publicoEsperado = this.publicoEsperado,
        condicionesParticipacion = this.condicionesParticipacion,
        rutaFotoEvidencia = this.rutaFotoEvidencia ?: ""
    )
}

fun EventoEntity.toUpdateDto(): EventoUpdateDto {
    return EventoUpdateDto(
        titulo = this.titulo,
        tipoEvento = this.tipoEvento,
        fecha = this.fecha,
        hora = this.hora,
        region = this.region,
        comuna = this.comuna,
        organizacionId = this.organizacionId,
        organizacionNombre = this.organizacionNombre,
        publicoEsperado = this.publicoEsperado,
        condicionesParticipacion = this.condicionesParticipacion,
        rutaFotoEvidencia = this.rutaFotoEvidencia
    )
}