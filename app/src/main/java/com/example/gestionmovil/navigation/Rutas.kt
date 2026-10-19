package com.example.gestionmovil.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Home : Rutas("home")

    // Ruta dinámica que recibe la ID de la oportunidad comercial:
    object Seguimiento : Rutas("seguimiento/{oportunidadId}") {
        fun crearRuta(oportunidadId: Long): String = "seguimiento/$oportunidadId"
    }

    object OportunidadList : Rutas("oportunidad_list")

    // Detalle de Oportunidad (recibe ID de la oportunidad)
    object OportunidadDetail : Rutas("oportunidad_detail/{oportunidadId}") {
        fun crearRuta(oportunidadId: Long): String = "oportunidad_detail/$oportunidadId"
    }

    // Formulario para Crear / Editar Oportunidad
    object OportunidadForm : Rutas("oportunidad_form")

    // Formulario para Registrar Contacto
    object ContactoForm : Rutas("contacto_form")

    // Gestión de Foto de Perfil
    object PerfilFoto : Rutas("perfil_foto")

    object CalendarioEventos : Rutas("calendario_eventos")
    object CrearEvento : Rutas("crear_evento")
    object DetalleEvento : Rutas("detalle_evento/{eventoId}") {
        fun crearRuta(eventoId: String) = "detalle_evento/$eventoId"
    }
    object Alertas : Rutas("alertas")
    object Historial : Rutas("historial")
}