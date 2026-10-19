package com.example.gestionmovil.utils

import android.util.Patterns

object Validaciones{

    //***INICIO validaciones Login y Registro:

    fun esCorreoValido(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun esNombreValido(nombre: String): Boolean {
        return nombre.trim().length >= 2
    }

    fun validarPassword(password: String): String? {
        if (password.length < 8) {
            return "La contraseña debe tener al menos 8 caracteres"
        }
        // .any evalúa que cualquier carácter dentro del String cumpla la condición.
        if (!password.any { it.isUpperCase() }) {
            return "La contraseña debe incluir al menos una letra mayúscula"
        }
        if (!password.any { it.isLowerCase() }) {
            return "La contraseña debe incluir al menos una letra minúscula"
        }
        if (!password.any { it.isDigit() }) {
            return "La contraseña debe incluir al menos un número"
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            return "La contraseña debe incluir al menos un carácter especial (ej. @, #, $, !)"
        }
        return null // Devuelve null si la contraseña cumple con todo
    }
    // *** FIN validaciones auxiliares ***


    // *** INICIO validaciones Login ***
    fun validarLogin(email: String, password: String): String? {
        if (email.isBlank() || password.isBlank()) {
            return "Todos los campos son obligatorios"
        }
        if (!esCorreoValido(email)) {
            return "El correo electrónico no tiene un formato válido"
        }

        if (validarPassword(password) != null){
            return validarPassword(password)
        }

        // Nota: En el Login no es necesario evaluar reglas complejas (mayúsculas, caracteres especiales),
        // solo que no esté vacía, ya que la base de datos se encargará de verificar si la clave coincide.
        return null
    }
    // *** FIN validaciones Login ***


    // *** INICIO validaciones Registro ***
    fun validarRegistro(
        pNombre: String,
        sNombre: String,
        pApellido: String,
        sApellido: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        // 1. Validar campos obligatorios
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || pNombre.isBlank() || pApellido.isBlank()) {
            return "Por favor completa los campos obligatorios"
        }

        // 2. Validar formato de nombres obligatorios
        if (!esNombreValido(pNombre) || !esNombreValido(pApellido)) {
            return "Los nombres y apellidos deben tener al menos 2 caracteres"
        }

        // 3. Validar correo
        if (!esCorreoValido(email)) {
            return "El correo electrónico no tiene un formato válido"
        }

        // 4. Validar complejidad de la contraseña
        val errorPassword = validarPassword(password)
        if (errorPassword != null) {
            return errorPassword
        }

        // 5. Validar que la contraseña y la confirmación coincidan
        if (password != confirmPassword) {
            return "Las contraseñas no coinciden"
        }

        return null // Todo correcto
    }
    //***FIN validaciones Login y Registro:


    // *** INICIO validaciones formularios Oportunidad/Contacto

    // Validar campo vacío.
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

    // *** FIN validaciones formularios Oportunidad/Contacto
}
