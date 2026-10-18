package com.example.gestionmovil.models

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")

data class Usuario (
    /*@PrimaryKey(autoGenerate = true)*/
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    //Se reeemplaza autoGenerate por UUID. Autogenerate delega a la BD el crear un ID en el INSERT. Podrían generarse 2 o más IDs idénticas en más de una solicitud a la vez. UUID genera un ID único.
    val pNombre: String = "",
    val sNombre: String? = null,
    val pApellido: String = "",
    val sApellido: String? = null,
    val fecNac: Long = 0L, //Como Long para no tener problemas con guardado en BD con SQLite.
    val email: String = "",
    val password: String = ""
)