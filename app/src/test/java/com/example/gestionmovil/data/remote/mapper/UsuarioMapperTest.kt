package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import org.junit.Assert.*
import org.junit.Test

class UsuarioMapperTest {

    @Test
    fun testToEntity() {
        val dto = UsuarioResponseDto(
            id = 100,
            email = "juan@example.com",
            primerNombre = "Juan",
            rol = "EJECUTIVO",
            token = "jwt-token-123"
        )

        val entity = dto.toEntity()

        assertEquals("100", entity.id)
        assertEquals("Juan", entity.pNombre)
        assertEquals("juan@example.com", entity.email)
        assertEquals("EJECUTIVO", entity.rol)
    }
}
