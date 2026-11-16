package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.remote.dto.OportunidadRequestDto
import com.example.gestionmovil.data.remote.dto.OportunidadResponseDto

// Convierte la respuesta del backend a entidad de base de datos local (Room)
fun OportunidadResponseDto.toEntity(): OportunidadEntity {
    return OportunidadEntity(
        id = this.id,
        nombre = this.nombre,
        tipoOportunidad = this.tipoOportunidad,
        descripcion = this.descripcion,
        fuenteInformacion = this.fuenteInformacion,
        fechaDeteccion = this.fechaDeteccion,
        responsableInterno = this.responsableInterno,
        prioridad = this.prioridad,
        potencialEstimado = this.potencialEstimado,
        estado = this.estado,
        fechaUltimaActualizacion = this.fechaUltimaActualizacion,
        organizacionId = this.organizacionId,
        observacion = this.observacion ?: ""
    )
}

// Para listas completas de oportunidades
fun List<OportunidadResponseDto>.toEntityList(): List<OportunidadEntity> {
    return this.map { it.toEntity() }
}

// Convierte la entidad local para enviarla como Request al backend
fun OportunidadEntity.toRequestDto(): OportunidadRequestDto {
    return OportunidadRequestDto(
        nombre = this.nombre,
        tipoOportunidad = this.tipoOportunidad,
        descripcion = this.descripcion,
        fuenteInformacion = this.fuenteInformacion,
        fechaDeteccion = this.fechaDeteccion,
        responsableInterno = this.responsableInterno,
        prioridad = this.prioridad,
        potencialEstimado = this.potencialEstimado,
        estado = this.estado,
        fechaUltimaActualizacion = this.fechaUltimaActualizacion,
        organizacionId = this.organizacionId,
        observacion = this.observacion
    )
}