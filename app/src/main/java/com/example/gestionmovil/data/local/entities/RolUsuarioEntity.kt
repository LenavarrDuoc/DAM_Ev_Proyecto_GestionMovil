package com.example.gestionmovil.data.local.entities

enum class RolUsuarioEntity(val descripcion: String) {
    ADMIN("Administrador"),
    SUPERVISOR("Supervisor Comercial"),
    EJECUTIVO("Ejecutivo Comercial"),
    CLIENTE("Cliente");

    fun esAdmin(): Boolean = this == ADMIN
    fun esSupervisor(): Boolean = this == SUPERVISOR
    fun esEjecutivo(): Boolean = this == EJECUTIVO
    fun esCliente(): Boolean = this == CLIENTE
}