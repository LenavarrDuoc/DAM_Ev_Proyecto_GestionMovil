package com.example.gestionmovil.utils

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

class ValidacionesTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            TestUtils.initPatterns()
        }
    }

    @Test
    fun testEsCorreoValido() {
        assertTrue(Validaciones.esCorreoValido("juan@example.com"))
        assertFalse(Validaciones.esCorreoValido(""))
        assertFalse(Validaciones.esCorreoValido("correo_invalido"))
    }

    @Test
    fun testEsNombreValido() {
        assertTrue(Validaciones.esNombreValido("Juan"))
        assertFalse(Validaciones.esNombreValido(" A "))
        assertFalse(Validaciones.esNombreValido(""))
    }

    @Test
    fun testValidarPassword_DemasiadoCorta() {
        val res = Validaciones.validarPassword("Ab1!")
        assertEquals("La contraseña debe tener al menos 8 caracteres", res)
    }

    @Test
    fun testValidarPassword_SinMayuscula() {
        val res = Validaciones.validarPassword("password123!")
        assertEquals("La contraseña debe incluir al menos una letra mayúscula", res)
    }

    @Test
    fun testValidarPassword_SinMinuscula() {
        val res = Validaciones.validarPassword("PASSWORD123!")
        assertEquals("La contraseña debe incluir al menos una letra minúscula", res)
    }

    @Test
    fun testValidarPassword_SinNumero() {
        val res = Validaciones.validarPassword("Password!")
        assertEquals("La contraseña debe incluir al menos un número", res)
    }

    @Test
    fun testValidarPassword_SinEspecial() {
        val res = Validaciones.validarPassword("Password123")
        assertEquals("La contraseña debe incluir al menos un carácter especial (ej. @, #, $, !)", res)
    }

    @Test
    fun testValidarPassword_Valida() {
        val res = Validaciones.validarPassword("Password123!")
        assertNull(res)
    }

    @Test
    fun testValidarLogin_TodasLasRamas() {
        // Campos vacíos
        assertEquals("Todos los campos son obligatorios", Validaciones.validarLogin("", "Password123!"))
        assertEquals("Todos los campos son obligatorios", Validaciones.validarLogin("juan@example.com", ""))

        // Correo inválido
        assertEquals("El correo electrónico no tiene un formato válido", Validaciones.validarLogin("invalido", "Password123!"))

        // Password inválida (falla complejidad)
        assertEquals("La contraseña debe tener al menos 8 caracteres", Validaciones.validarLogin("juan@example.com", "Ab1!"))

        // Éxito
        assertNull(Validaciones.validarLogin("juan@example.com", "Password123!"))
    }

    @Test
    fun testValidarRegistro_TodasLasRamas() {
        // Campos obligatorios faltantes
        assertEquals("Por favor completa los campos obligatorios", Validaciones.validarRegistro("", "Carlos", "Pérez", "", "test@example.com", "Password123!", "Password123!"))

        // Formato nombres inválido
        assertEquals("Los nombres y apellidos deben tener al menos 2 caracteres", Validaciones.validarRegistro("A", "", "Pérez", "", "test@domain.com", "Password123!", "Password123!"))

        // Correo inválido
        assertEquals("El correo electrónico no tiene un formato válido", Validaciones.validarRegistro("Juan", "", "Pérez", "", "bademail", "Password123!", "Password123!"))

        // Password débil
        assertEquals("La contraseña debe tener al menos 8 caracteres", Validaciones.validarRegistro("Juan", "", "Pérez", "", "juan@domain.com", "123", "123"))

        // Passwords no coinciden
        assertEquals("Las contraseñas no coinciden", Validaciones.validarRegistro("Juan", "", "Pérez", "", "juan@domain.com", "Password123!", "Password999!"))

        // Éxito
        assertNull(Validaciones.validarRegistro("Juan", "Carlos", "Pérez", "Gómez", "juan@domain.com", "Password123!", "Password123!"))
    }

    @Test
    fun testValidarNotEmpty() {
        assertNotNull(Validaciones.validarNotEmpty("", "Nombre"))
        assertNull(Validaciones.validarNotEmpty("Valor", "Nombre"))
    }

    @Test
    fun testValidarCorreo() {
        assertEquals("El correo es obligatorio", Validaciones.validarCorreo(""))
        assertEquals("Ingrese un correo ficticio válido", Validaciones.validarCorreo("correo_invalido"))
        assertNull(Validaciones.validarCorreo("test@domain.com"))
    }

    @Test
    fun testValidarTelefono() {
        assertEquals("El teléfono es obligatorio", Validaciones.validarTelefono(""))
        assertEquals("El teléfono debe tener al menos 8 dígitos", Validaciones.validarTelefono("123"))
        assertNull(Validaciones.validarTelefono("+56912345678"))
    }
}
