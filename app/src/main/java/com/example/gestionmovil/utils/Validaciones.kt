package com.example.gestionmovil.utils

object Validaciones {
    fun validarNotEmpty(value: String, nombreCampo: String): String? {
        return if (value.isBlank()) "El campo $nombreCampo no puede estar vacío" else null
    }

    fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return "El correo es obligatorio"
        val correoRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return if (!correo.matches(correoRegex)) "Ingrese un correo ficticio válido" else null
    }

    fun validarTelefono(telefono: String): String? {
        if (telefono.isBlank()) return "El teléfono es obligatorio"
        return if (telefono.length < 8) "El teléfono debe tener al menos 8 dígitos" else null
    }
}