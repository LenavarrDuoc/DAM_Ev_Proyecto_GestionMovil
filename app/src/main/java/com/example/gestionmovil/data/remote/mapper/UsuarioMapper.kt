package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.remote.dto.UsuarioRequestDto
import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import com.example.gestionmovil.data.remote.dto.UsuarioUpdateDto

// 1. De DTO Response (Backend) -> Entity (Room)
fun UsuarioResponseDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = this.id, // String
        pNombre = this.pNombre ?: "",
        sNombre = this.sNombre ?: "",
        pApellido = this.pApellido ?: "",
        sApellido = this.sApellido ?: "",
        fecNac = this.fecNac ?: 0L,
        email = this.email ?: "",
        rol = this.rol ?: "CLIENTE", // Valor por defecto sensato
        fotoUri = this.fotoUri ?: ""
        // No hay contraseña en la respuesta, así que no se guarda en Room
    )
}

// 2. De Lista de DTOs Response (Backend) -> Lista de Entities (Room)
fun List<UsuarioResponseDto>.toEntityList(): List<UsuarioEntity> {
    return this.map { it.toEntity() }
}

// 3. De Entity (Room) -> DTO Request (Para POST en Backend)
fun UsuarioEntity.toRequestDto(): UsuarioRequestDto {
    return UsuarioRequestDto(
        pNombre = this.pNombre,
        sNombre = this.sNombre ?: "",
        pApellido = this.pApellido,
        sApellido = this.sApellido ?: "",
        fecNac = this.fecNac,
        email = this.email,
        password = "", // La contraseña normalmente se envía desde la Vista (ViewModel), no desde Room
        rol = this.rol,
        fotoUri = this.fotoUri ?: ""
    )
}

// 4. De Entity (Room) -> DTO Update (Para PUT en Backend)
fun UsuarioEntity.toUpdateDto(): UsuarioUpdateDto {
    return UsuarioUpdateDto(
        pNombre = this.pNombre,
        sNombre = this.sNombre ?: "",
        pApellido = this.pApellido,
        sApellido = this.sApellido ?: "",
        fecNac = this.fecNac,
        rol = this.rol,
        fotoUri = this.fotoUri ?: ""
    )
}