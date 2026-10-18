package com.example.gestionmovil.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Home : Rutas("home")
    // Ruta dinámica que recibe la ID de la oportunidad comercial:
    object Seguimiento : Rutas("seguimiento/{oportunidadId}") {
        fun crearRuta(oportunidadId: Long): String = "seguimiento/$oportunidadId"
    }
}