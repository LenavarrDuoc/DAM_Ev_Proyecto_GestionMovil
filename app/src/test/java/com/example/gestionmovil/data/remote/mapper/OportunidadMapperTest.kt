package com.example.gestionmovil.data.remote.mapper

import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.remote.dto.OportunidadResponseDto
import org.junit.Assert.*
import org.junit.Test

class OportunidadMapperTest {

    @Test
    fun testToEntity() {
        val dto = OportunidadResponseDto(
            id = 10L,
            nombre = "Venta Software",
            tipoOportunidad = "Licencia",
            descripcion = "Venta de 10 licencias",
            fuenteInformacion = "Web",
            fechaDeteccion = "2025-01-01",
            responsableInterno = "Carlos",
            prioridad = "Alta",
            potencialEstimado = "$10,000",
            estado = "En Proceso",
            fechaUltimaActualizacion = "2025-01-02",
            organizacionId = 1L,
            observacion = "Sin notas"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.nombre, entity.nombre)
        assertEquals(dto.tipoOportunidad, entity.tipoOportunidad)
        assertEquals(dto.descripcion, entity.descripcion)
        assertEquals(dto.fuenteInformacion, entity.fuenteInformacion)
        assertEquals(dto.fechaDeteccion, entity.fechaDeteccion)
        assertEquals(dto.responsableInterno, entity.responsableInterno)
        assertEquals(dto.prioridad, entity.prioridad)
        assertEquals(dto.potencialEstimado, entity.potencialEstimado)
        assertEquals(dto.estado, entity.estado)
        assertEquals(dto.fechaUltimaActualizacion, entity.fechaUltimaActualizacion)
        assertEquals(dto.organizacionId, entity.organizacionId)
        assertEquals("Sin notas", entity.observacion)
    }

    @Test
    fun testToEntityList() {
        val dtos = listOf(
            OportunidadResponseDto(
                id = 1L, nombre = "Op 1", tipoOportunidad = "Tipo", descripcion = "Desc",
                fuenteInformacion = "F", fechaDeteccion = "F", responsableInterno = "R",
                prioridad = "P", potencialEstimado = "P", estado = "E",
                fechaUltimaActualizacion = "F", organizacionId = 1L, observacion = null
            )
        )

        val entities = dtos.toEntityList()
        assertEquals(1, entities.size)
        assertEquals(1L, entities[0].id)
        assertEquals("", entities[0].observacion)
    }

    @Test
    fun testToRequestDto() {
        val entity = OportunidadEntity(
            id = 5L,
            nombre = "Op Test",
            tipoOportunidad = "Consultoría",
            descripcion = "Servicios",
            fuenteInformacion = "Llamada",
            fechaDeteccion = "2025-02-01",
            responsableInterno = "Ana",
            prioridad = "Media",
            potencialEstimado = "$5000",
            estado = "Nueva",
            fechaUltimaActualizacion = "2025-02-01",
            organizacionId = 2L,
            observacion = "Prueba"
        )

        val dto = entity.toRequestDto()

        assertEquals(entity.nombre, dto.nombre)
        assertEquals(entity.tipoOportunidad, dto.tipoOportunidad)
        assertEquals(entity.descripcion, dto.descripcion)
        assertEquals(entity.fuenteInformacion, dto.fuenteInformacion)
        assertEquals(entity.fechaDeteccion, dto.fechaDeteccion)
        assertEquals(entity.responsableInterno, dto.responsableInterno)
        assertEquals(entity.prioridad, dto.prioridad)
        assertEquals(entity.potencialEstimado, dto.potencialEstimado)
        assertEquals(entity.estado, dto.estado)
        assertEquals(entity.fechaUltimaActualizacion, dto.fechaUltimaActualizacion)
        assertEquals(entity.organizacionId, dto.organizacionId)
        assertEquals(entity.observacion, dto.observacion)
    }
}
