package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.remote.dto.ContactoRequestDto
import com.example.gestionmovil.data.remote.dto.ContactoResponseDto
import com.example.gestionmovil.data.remote.dto.ContactoUpdateDto

fun ContactoResponseDto.toEntity(): ContactoEntity {
    return ContactoEntity(
        id = this.id,
        nombre = this.nombre ?: "",
        organizacionId = this.organizacionId ?: 0L,
        cargo = this.cargo ?: "",
        area = this.area ?: "",
        correo = this.correo ?: "",
        telefono = this.telefono ?: "",
        medioPreferente = this.medioPreferente ?: "",
        estado = this.estado ?: "",
        fechaUltimoContacto = this.fechaUltimoContacto ?: "",
        observacion = this.observacion ?: ""
    )
}

fun List<ContactoResponseDto>.toEntityList(): List<ContactoEntity> {
    return this.map { it.toEntity() }
}

fun ContactoEntity.toRequestDto(): ContactoRequestDto {
    return ContactoRequestDto(
        nombre = this.nombre,
        organizacionId = this.organizacionId,
        cargo = this.cargo,
        area = this.area,
        correo = this.correo,
        telefono = this.telefono,
        medioPreferente = this.medioPreferente,
        estado = this.estado,
        fechaUltimoContacto = this.fechaUltimoContacto,
        observacion = this.observacion
    )
}

fun ContactoEntity.toUpdateDto(): ContactoUpdateDto {
    return ContactoUpdateDto(
        nombre = this.nombre,
        organizacionId = this.organizacionId,
        cargo = this.cargo,
        area = this.area,
        correo = this.correo,
        telefono = this.telefono,
        medioPreferente = this.medioPreferente,
        estado = this.estado,
        fechaUltimoContacto = this.fechaUltimoContacto,
        observacion = this.observacion
    )
}