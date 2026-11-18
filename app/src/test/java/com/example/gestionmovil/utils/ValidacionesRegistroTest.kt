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
        assertEquals("El primer nombre es obligatorio", ValidacionesRegistro.validarPrimerNombre(""))
        assertEquals("Debe tener al menos 2 caracteres", ValidacionesRegistro.validarPrimerNombre(" A "))
        assertNull(ValidacionesRegistro.validarPrimerNombre("Juan"))
    }

    @Test
    fun testValidarSegundoNombre() {
        assertNull(ValidacionesRegistro.validarSegundoNombre(""))
        assertEquals("Debe tener al menos 2 caracteres", ValidacionesRegistro.validarSegundoNombre(" A "))
        assertNull(ValidacionesRegistro.validarSegundoNombre("Carlos"))
    }

    @Test
    fun testValidarPrimerApellido() {
        assertEquals("El primer apellido es obligatorio", ValidacionesRegistro.validarPrimerApellido(""))
        assertEquals("Debe tener al menos 2 caracteres", ValidacionesRegistro.validarPrimerApellido(" B "))
        assertNull(ValidacionesRegistro.validarPrimerApellido("Pérez"))
    }

    @Test
    fun testValidarSegundoApellido() {
        assertNull(ValidacionesRegistro.validarSegundoApellido(""))
        assertEquals("Debe tener al menos 2 caracteres", ValidacionesRegistro.validarSegundoApellido(" C "))
        assertNull(ValidacionesRegistro.validarSegundoApellido("Gómez"))
    }

    @Test
    fun testValidarFechaNacimiento() {
        assertEquals("Debe seleccionar su fecha de nacimiento", ValidacionesRegistro.validarFechaNacimiento(null))

        // Menor de 18 años por año
        val calMenor10 = Calendar.getInstance().apply {
            add(Calendar.YEAR, -10)
        }
        assertEquals("Debes ser mayor de 18 años para registrarte", ValidacionesRegistro.validarFechaNacimiento(calMenor10.time))

        // Cumple 18 años mañana (edad calculada = 18, pero no ha cumplido años este año -> edad baja a 17)
        val calManana18 = Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        assertEquals("Debes ser mayor de 18 años para registrarte", ValidacionesRegistro.validarFechaNacimiento(calManana18.time))

        // Mayor de 18 años
        val calMayor = Calendar.getInstance().apply {
            add(Calendar.YEAR, -25)
        }
        assertNull(ValidacionesRegistro.validarFechaNacimiento(calMayor.time))
    }

    @Test
    fun testValidarEmail() {
        assertEquals("El correo electrónico es obligatorio", ValidacionesRegistro.validarEmail(""))
        assertEquals("Formato de correo inválido", ValidacionesRegistro.validarEmail("correo_invalido"))
        assertNull(ValidacionesRegistro.validarEmail("test@example.com"))
    }

    @Test
    fun testValidarPassword() {
        assertEquals("La contraseña es obligatoria", ValidacionesRegistro.validarPassword(""))
        assertEquals("Mínimo 8 caracteres", ValidacionesRegistro.validarPassword("123"))
        assertEquals("Debe incluir al menos una mayúscula", ValidacionesRegistro.validarPassword("password123!"))
        assertEquals("Debe incluir al menos una minúscula", ValidacionesRegistro.validarPassword("PASSWORD123!"))
        assertEquals("Debe incluir al menos un número", ValidacionesRegistro.validarPassword("Password!"))
        assertEquals("Debe incluir un carácter especial (ej. @, #, $)", ValidacionesRegistro.validarPassword("Password123"))
        assertNull(ValidacionesRegistro.validarPassword("Password123!"))
    }

    @Test
    fun testValidarConfirmPassword() {
        assertEquals("Debe confirmar la contraseña", ValidacionesRegistro.validarConfirmPassword("Password123!", ""))
        assertEquals("Las contraseñas no coinciden", ValidacionesRegistro.validarConfirmPassword("Password123!", "Otra123!"))
        assertNull(ValidacionesRegistro.validarConfirmPassword("Password123!", "Password123!"))
    }

    @Test
    fun testValidarTerminos() {
        assertEquals("Debe aceptar los términos y condiciones", ValidacionesRegistro.validarTerminos(false))
        assertNull(ValidacionesRegistro.validarTerminos(true))
    }
}
