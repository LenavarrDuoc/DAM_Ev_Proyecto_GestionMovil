package com.example.gestionmovil.utils

import android.util.Patterns
import java.util.Calendar
import java.util.Date

object ValidacionesRegistro {

    fun validarPrimerNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El primer nombre es obligatorio"
            nombre.trim().length < 2 -> "Debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun validarSegundoNombre(nombre: String): String? {
        return when {
            nombre.isNotBlank() && nombre.trim().length < 2 -> "Debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun validarPrimerApellido(apellido: String): String? {
        return when {
            apellido.isBlank() -> "El primer apellido es obligatorio"
            apellido.trim().length < 2 -> "Debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun validarSegundoApellido(apellido: String): String? {
        return when {
            apellido.isNotBlank() && apellido.trim().length < 2 -> "Debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun validarFechaNacimiento(date: Date?): String? {
        if (date == null) return "Debe seleccionar su fecha de nacimiento"

        val hoy = Calendar.getInstance()
        val nac = Calendar.getInstance().apply { time = date }
        var edad = hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR)

        if (hoy.get(Calendar.DAY_OF_YEAR) < nac.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }

        return if (edad < 18) "Debes ser mayor de 18 años para registrarte" else null
    }

    fun validarEmail(email: String): String? {
        return when {
            email.isBlank() -> "El correo electrónico es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de correo inválido"
            else -> null
        }
    }

    fun validarPassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es obligatoria"
            password.length < 8 -> "Mínimo 8 caracteres"
            !password.any { it.isUpperCase() } -> "Debe incluir al menos una mayúscula"
            !password.any { it.isLowerCase() } -> "Debe incluir al menos una minúscula"
            !password.any { it.isDigit() } -> "Debe incluir al menos un número"
            !password.any { !it.isLetterOrDigit() } -> "Debe incluir un carácter especial (ej. @, #, $)"
            else -> null
        }
    }

    fun validarConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Debe confirmar la contraseña"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    fun validarTerminos(acepta: Boolean): String? {
        return if (!acepta) "Debe aceptar los términos y condiciones" else null
    }
}