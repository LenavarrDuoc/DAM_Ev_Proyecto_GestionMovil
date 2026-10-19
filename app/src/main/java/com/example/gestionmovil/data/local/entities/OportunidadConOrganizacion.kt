package com.example.gestionmovil.data.local.entities

import androidx.room3.Embedded
import androidx.room3.Relation

data class OportunidadConOrganizacion(
    @Embedded
    val oportunidad: OportunidadEntity,

    @Relation(
        parentColumns = ["organizacionId"],
        entityColumns = ["id"],
        entity = OrganizacionEntity::class
    )
    val organizacion: OrganizacionEntity?
)