package com.example.gestionmovil.utils

import java.security.MessageDigest


object Seguridad {

    //Hasheo de la contraseña de usuario para Login.
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}