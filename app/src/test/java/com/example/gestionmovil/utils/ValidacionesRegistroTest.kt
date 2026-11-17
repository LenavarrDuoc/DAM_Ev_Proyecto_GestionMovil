package com.example.gestionmovil.utils

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import java.util.Calendar

class ValidacionesRegistroTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            TestUtils.initPatterns()
        }
    }

    @Test
    fun testValidarPrimerNombre() {
        assertNotNull(ValidacionesRegistro.validarPrimerNombre(""))
        assertNotNull(ValidacionesRegistro.validarPrimerNombre(" A "))
        assertNull(ValidacionesRegistro.validarPrimerNombre("Juan"))
    }

    @Test
    fun testValidarSegundoNombre() {
        assertNull(ValidacionesRegistro.validarSegundoNombre(""))
        assertNotNull(ValidacionesRegistro.validarSegundoNombre(" A "))
        assertNull(ValidacionesRegistro.validarSegundoNombre("Carlos"))
    }

    @Test
    fun testValidarPrimerApellido() {
        assertNotNull(ValidacionesRegistro.validarPrimerApellido(""))
        assertNotNull(ValidacionesRegistro.validarPrimerApellido(" B "))
        assertNull(ValidacionesRegistro.validarPrimerApellido("Pérez"))
    }

    @Test
    fun testValidarSegundoApellido() {
        assertNull(ValidacionesRegistro.validarSegundoApellido(""))
        assertNotNull(ValidacionesRegistro.validarSegundoApellido(" C "))
        assertNull(ValidacionesRegistro.validarSegundoApellido("Gómez"))
    }

    @Test
    fun testValidarFechaNacimiento() {
        assertNotNull(ValidacionesRegistro.validarFechaNacimiento(null))

        val calMenor = Calendar.getInstance().apply {
            add(Calendar.YEAR, -10)
        }
        assertNotNull(ValidacionesRegistro.validarFechaNacimiento(calMenor.time))

        val calMayor = Calendar.getInstance().apply {
            add(Calendar.YEAR, -25)
        }
        assertNull(ValidacionesRegistro.validarFechaNacimiento(calMayor.time))
    }

    @Test
    fun testValidarPassword() {
        assertNotNull(ValidacionesRegistro.validarPassword(""))
        assertNotNull(ValidacionesRegistro.validarPassword("123"))
        assertNotNull(ValidacionesRegistro.validarPassword("password123!"))
        assertNull(ValidacionesRegistro.validarPassword("Password123!"))
    }

    @Test
    fun testValidarConfirmPassword() {
        assertNotNull(ValidacionesRegistro.validarConfirmPassword("Password123!", ""))
        assertNotNull(ValidacionesRegistro.validarConfirmPassword("Password123!", "Otra123!"))
        assertNull(ValidacionesRegistro.validarConfirmPassword("Password123!", "Password123!"))
    }

    @Test
    fun testValidarTerminos() {
        assertNotNull(ValidacionesRegistro.validarTerminos(false))
        assertNull(ValidacionesRegistro.validarTerminos(true))
    }
}
