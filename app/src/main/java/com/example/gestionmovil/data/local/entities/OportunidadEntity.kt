package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "oportunidades",
    foreignKeys = [
        ForeignKey(
            entity = OrganizacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["organizacionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["organizacionId"])]
)
data class OportunidadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val tipoOportunidad: String,
    val descripcion: String,
    val fuenteInformacion: String,
    val fechaDeteccion: String,
    val responsableInterno: String,
    val prioridad: String,
    val potencialEstimado: String,
    val estado: String,
    val fechaUltimaActualizacion: String,
    val organizacionId: Long? = null,
    val observacion: String = ""

)