package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.remote.dto.SeguimientoRequestDto
import com.example.gestionmovil.data.remote.dto.SeguimientoResponseDto
import com.example.gestionmovil.data.remote.dto.SeguimientoUpdateDto

// 1. De DTO Response (Backend) -> Entity (Room)
fun SeguimientoResponseDto.toEntity(): SeguimientoEntity {
    return SeguimientoEntity(
        id = this.id,
        oportunidadId = this.oportunidadId ?: 0L,
        contactoId = this.contactoId, // Si tu Room no acepta nulos aquí, ponle ?: 0L
        usuarioEmail = this.usuarioEmail ?: "",
        tipoGestion = this.tipoGestion ?: "",
        propuesta = this.propuesta ?: "",
        respuestaCliente = this.respuestaCliente ?: "",
        proximaAccion = this.proximaAccion ?: "",
        fechaGestion = this.fechaGestion ?: "",
        fechaProximaAccion = this.fechaProximaAccion ?: "",
        observacion = this.observacion ?: ""
    )
}

// 2. De Lista de DTOs Response (Backend) -> Lista de Entities (Room)
fun List<SeguimientoResponseDto>.toEntityList(): List<SeguimientoEntity> {
    return this.map { it.toEntity() }
}

// 3. De Entity (Room) -> DTO Request (Para POST en Backend)
fun SeguimientoEntity.toRequestDto(): SeguimientoRequestDto {
    return SeguimientoRequestDto(
        oportunidadId = this.oportunidadId,
        contactoId = this.contactoId,
        usuarioEmail = this.usuarioEmail,
        tipoGestion = this.tipoGestion,
        propuesta = this.propuesta,
        respuestaCliente = this.respuestaCliente,
        proximaAccion = this.proximaAccion,
        fechaGestion = this.fechaGestion,
        fechaProximaAccion = this.fechaProximaAccion,
        observacion = this.observacion
    )
}

// 4. De Entity (Room) -> DTO Update (Para PUT en Backend)
fun SeguimientoEntity.toUpdateDto(): SeguimientoUpdateDto {
    return SeguimientoUpdateDto(
        oportunidadId = this.oportunidadId,
        contactoId = this.contactoId,
        usuarioEmail = this.usuarioEmail,
        tipoGestion = this.tipoGestion,
        propuesta = this.propuesta,
        respuestaCliente = this.respuestaCliente,
        proximaAccion = this.proximaAccion,
        fechaGestion = this.fechaGestion,
        fechaProximaAccion = this.fechaProximaAccion,
        observacion = this.observacion
    )
}