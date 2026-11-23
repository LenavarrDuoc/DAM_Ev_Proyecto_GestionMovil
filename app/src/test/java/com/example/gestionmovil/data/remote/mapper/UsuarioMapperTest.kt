package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import org.junit.Assert.*
import org.junit.Test

class UsuarioMapperTest {

    @Test
    fun convertirUsuarioDtoAEntidad() {
        val dto = UsuarioResponseDto(
            id = "100",
            pNombre = "Juan",
            sNombre = "Carlos",
            pApellido = "Pérez",
            sApellido = "Gómez",
            fecNac = 800000000000L,
            email = "juan@example.com",
            rol = "EJECUTIVO",
            fotoUri = "file:///foto.jpg"
        )

        val entity = dto.toEntity()

        assertEquals("100", entity.id)
        assertEquals("Juan", entity.pNombre)
        assertEquals("juan@example.com", entity.email)
        assertEquals("EJECUTIVO", entity.rol)
        assertEquals("file:///foto.jpg", entity.fotoUri)
    }
}
