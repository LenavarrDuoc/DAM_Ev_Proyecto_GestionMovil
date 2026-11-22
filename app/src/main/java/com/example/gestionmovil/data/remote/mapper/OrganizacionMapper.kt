package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.remote.dto.OrganizacionRequestDto
import com.example.gestionmovil.data.remote.dto.OrganizacionResponseDto
import com.example.gestionmovil.data.remote.dto.OrganizacionUpdateDto

// 1. De DTO Response (Backend) -> Entity (Room)
fun OrganizacionResponseDto.toEntity(): OrganizacionEntity {
    return OrganizacionEntity(
        id = this.id,
        nombre = this.nombre,
        tipoOrganizacion = this.tipoOrganizacion,
        rubro = this.rubro,
        comuna = this.comuna,
        region = this.region,
        website = this.website ?: "",
        audienciaObjetivo = this.audienciaObjetivo ?: "",
        observacion = this.observacion ?: ""
    )
}

// 2. De Lista de DTOs Response (Backend) -> Lista de Entities (Room)
fun List<OrganizacionResponseDto>.toEntityList(): List<OrganizacionEntity> {
    return this.map { it.toEntity() }
}

// 3. De Entity (Room) -> DTO Request (Para POST en Backend)
fun OrganizacionEntity.toRequestDto(): OrganizacionRequestDto {
    return OrganizacionRequestDto(
        nombre = this.nombre,
        tipoOrganizacion = this.tipoOrganizacion,
        rubro = this.rubro,
        comuna = this.comuna,
        region = this.region,
        website = this.website,
        audienciaObjetivo = this.audienciaObjetivo,
        observacion = this.observacion
    )
}

// 4. De Entity (Room) -> DTO Update (Para PUT en Backend)
fun OrganizacionEntity.toUpdateDto(): OrganizacionUpdateDto {
    return OrganizacionUpdateDto(
        nombre = this.nombre,
        tipoOrganizacion = this.tipoOrganizacion,
        rubro = this.rubro,
        comuna = this.comuna,
        region = this.region,
        website = this.website,
        audienciaObjetivo = this.audienciaObjetivo,
        observacion = this.observacion
    )
}