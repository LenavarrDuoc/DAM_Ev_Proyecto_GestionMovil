package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "contactos",
    foreignKeys = [
        ForeignKey(
            entity = OrganizacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["organizacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["organizacionId"])]
)
data class ContactoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val organizacionId: Long,
    val cargo: String,
    val area: String,
    val correo: String,
    val telefono: String,
    val medioPreferente: String,
    val estado: String,
    val fechaUltimoContacto: String,
    val observacion: String = ""
)