package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto

// Convierte la respuesta del login del backend a entidad local
fun UsuarioResponseDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = this.id.toString(),
        pNombre = this.primerNombre,
        email = this.email,
        rol = this.rol
    )
}