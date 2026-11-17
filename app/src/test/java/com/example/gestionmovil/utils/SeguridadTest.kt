package com.example.gestionmovil.utils

import org.junit.Assert.*
import org.junit.Test

class SeguridadTest {

    @Test
    fun testHashPassword_Consistency() {
        val hash1 = Seguridad.hashPassword("Password123!")
        val hash2 = Seguridad.hashPassword("Password123!")
        val hash3 = Seguridad.hashPassword("Diferente123!")

        assertNotNull(hash1)
        assertEquals(64, hash1.length) // SHA-256 hex string tiene 64 caracteres
        assertEquals(hash1, hash2)
        assertNotEquals(hash1, hash3)
    }
}
