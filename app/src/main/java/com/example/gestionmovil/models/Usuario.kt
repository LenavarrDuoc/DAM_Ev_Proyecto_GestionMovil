package com.example.gestionmovil.models

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey
    val id: String,
    val pNombre: String = "",
    val sNombre: String = "",
    val pApellido: String = "",
    val sApellido: String = "",
    val email: String,
    val password: String = ""
)