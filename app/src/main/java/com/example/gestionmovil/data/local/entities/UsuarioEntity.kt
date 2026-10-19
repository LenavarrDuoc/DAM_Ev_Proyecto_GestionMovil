package com.example.gestionmovil.data.local.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val pNombre: String = "",
    val sNombre: String? = null,
    val pApellido: String = "",
    val sApellido: String? = null,
    val fecNac: Long = 0L,
    val email: String = "",
    val password: String = "",
    val rol: String = RolUsuarioEntity.CLIENTE.name,
    val fotoUri: String? = null // <-- Guarda la URI local de la imagen de perfil
) {
    fun esAdmin(): Boolean = rol.equals(RolUsuarioEntity.ADMIN.name, ignoreCase = true)
    fun esSupervisor(): Boolean = rol.equals(RolUsuarioEntity.SUPERVISOR.name, ignoreCase = true)
    fun esEjecutivo(): Boolean = rol.equals(RolUsuarioEntity.EJECUTIVO.name, ignoreCase = true)
    fun esCliente(): Boolean = rol.equals(RolUsuarioEntity.CLIENTE.name, ignoreCase = true)
}