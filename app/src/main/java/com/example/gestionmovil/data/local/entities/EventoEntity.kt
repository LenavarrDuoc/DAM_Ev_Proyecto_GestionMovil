package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val tipoEvento: String, // ej: Conferencia, Feria, Reunión, Seminario
    val fecha: String, // YYYY-MM-DD
    val hora: String, // HH:mm
    val region: String,
    val comuna: String,
    val organizacionId: String,
    val organizacionNombre: String,
    val publicoEsperado: Int,
    val condicionesParticipacion: String,
    val rutaFotoEvidencia: String? = null // Ruta o indicador de la foto capturada
)