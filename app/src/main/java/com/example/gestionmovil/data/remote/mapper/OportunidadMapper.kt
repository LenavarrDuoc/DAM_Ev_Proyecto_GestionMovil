package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.remote.dto.OportunidadRequestDto
import com.example.gestionmovil.data.remote.dto.OportunidadResponseDto
import com.example.gestionmovil.data.remote.dto.OportunidadUpdateDto

// 1. De DTO Response (Backend) -> Entity (Room)
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

// 2. De Lista de DTOs Response (Backend) -> Lista de Entities (Room)
fun List<OportunidadResponseDto>.toEntityList(): List<OportunidadEntity> {
    return this.map { it.toEntity() }
}

// 3. De Entity (Room) -> DTO Request (Para POST en Backend)
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

// 4. De Entity (Room) -> DTO Update (Para PUT en Backend)
fun OportunidadEntity.toUpdateDto(): OportunidadUpdateDto {
    return OportunidadUpdateDto(
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