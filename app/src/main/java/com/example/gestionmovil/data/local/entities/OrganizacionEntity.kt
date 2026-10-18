package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "organizaciones")
data class OrganizacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val tipoOrganizacion: String,
    val rubro: String,
    val comuna: String,
    val region: String,
    val website: String = "",
    val audienciaObjetivo: String = "",
    val observacion: String = ""
)