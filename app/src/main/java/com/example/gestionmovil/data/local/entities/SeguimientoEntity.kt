package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "seguimientos",
    foreignKeys = [
        ForeignKey(
            entity = OportunidadEntity::class,
            parentColumns = ["id"],
            childColumns = ["oportunidadId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContactoEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["oportunidadId"]),
        Index(value = ["contactoId"])
    ]
)
data class SeguimientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val oportunidadId: Long,
    val contactoId: Long? = null,
    val usuarioEmail: String = "",        // Vendedor/usuario que realiza la gestión
    val tipoGestion: String,             // Llamada, Correo, Reunión, Propuesta, Visita, etc.
    val propuesta: String = "",          // Detalle de la propuesta comercial u oferta
    val respuestaCliente: String = "",   // Feedback o respuesta dada por el cliente
    val proximaAccion: String = "",      // Siguiente compromiso o tarea pendiente
    val fechaGestion: String,            // Fecha en que se realizó la gestión (AAAA-MM-DD)
    val fechaProximaAccion: String = "", // Fecha programada para la próxima acción
    val observacion: String = ""
)