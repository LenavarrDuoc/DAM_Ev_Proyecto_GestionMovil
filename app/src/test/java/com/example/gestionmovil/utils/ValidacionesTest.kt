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
    fun testValidarLogin_CamposVacios() {
        val res = Validaciones.validarLogin("", "")
        assertEquals("Todos los campos son obligatorios", res)
    }

    @Test
    fun testValidarRegistro_CamposIncompletos() {
        val res = Validaciones.validarRegistro("", "Carlos", "Pérez", "", "test@example.com", "Password123!", "Password123!")
        assertEquals("Por favor completa los campos obligatorios", res)
    }

    @Test
    fun testValidarRegistro_NombresCortos() {
        val res = Validaciones.validarRegistro("A", "", "B", "", "test@domain.com", "Password123!", "Password123!")
        assertEquals("Los nombres y apellidos deben tener al menos 2 caracteres", res)
    }

    @Test
    fun testValidarRegistro_PasswordsNoCoinciden() {
        val res = Validaciones.validarRegistro("Juan", "", "Pérez", "", "juan@domain.com", "Password123!", "Password999!")
        assertEquals("Las contraseñas no coinciden", res)
    }

    @Test
    fun testValidarNotEmpty() {
        assertNotNull(Validaciones.validarNotEmpty("", "Nombre"))
        assertNull(Validaciones.validarNotEmpty("Valor", "Nombre"))
    }

    @Test
    fun testValidarCorreo() {
        assertNotNull(Validaciones.validarCorreo(""))
        assertNotNull(Validaciones.validarCorreo("correo_invalido"))
        assertNull(Validaciones.validarCorreo("test@domain.com"))
    }

    @Test
    fun testValidarTelefono() {
        assertNotNull(Validaciones.validarTelefono(""))
        assertNotNull(Validaciones.validarTelefono("123"))
        assertNull(Validaciones.validarTelefono("+56912345678"))
    }
}
