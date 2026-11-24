package com.example.gestionmovil.fakes

import com.example.gestionmovil.data.remote.api.*
import com.example.gestionmovil.data.remote.dto.*
import retrofit2.Response
import java.io.IOException

class FakeAuthApiService : AuthApiService {
    var shouldFail = true
    var loginResponse: UsuarioResponseDto? = null

    override suspend fun login(request: LoginRequestDto): Response<UsuarioResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        val res = loginResponse ?: throw IOException("Offline test mode")
        return Response.success(res)
    }
}

class FakeContactoApiService : ContactoApiService {
    var shouldFail = true
    override suspend fun create(input: ContactoRequestDto): Response<ContactoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(ContactoResponseDto(1L, input.nombre, input.organizacionId, input.cargo, input.area, input.correo, input.telefono, input.medioPreferente, input.estado, input.fechaUltimoContacto, input.observacion))
    }
    override suspend fun findAll(): Response<List<ContactoResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: Long): Response<ContactoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(ContactoResponseDto(id, "", 1L, "", "", "", "", "", "", "", ""))
    }
    override suspend fun update(id: Long, input: ContactoUpdateDto): Response<ContactoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(ContactoResponseDto(id, input.nombre, input.organizacionId, input.cargo, input.area, input.correo, input.telefono, input.medioPreferente, input.estado, input.fechaUltimoContacto, input.observacion))
    }
    override suspend fun delete(id: Long): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}

class FakeEventoApiService : EventoApiService {
    var shouldFail = true
    override suspend fun create(input: EventoRequestDto): Response<EventoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(EventoResponseDto("1", input.titulo, input.tipoEvento, input.fecha, input.hora, input.region, input.comuna, input.organizacionId, input.organizacionNombre, input.publicoEsperado, input.condicionesParticipacion, input.rutaFotoEvidencia))
    }
    override suspend fun findAll(): Response<List<EventoResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: String): Response<EventoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(EventoResponseDto(id, "", "", "", "", "", "", null, "", 0, "", ""))
    }
    override suspend fun update(id: String, input: EventoUpdateDto): Response<EventoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(EventoResponseDto(id, input.titulo, input.tipoEvento, input.fecha, input.hora, input.region, input.comuna, input.organizacionId, input.organizacionNombre, input.publicoEsperado, input.condicionesParticipacion, input.rutaFotoEvidencia))
    }
    override suspend fun delete(id: String): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}

class FakeOportunidadApiService : OportunidadApiService {
    var shouldFail = true
    override suspend fun create(input: OportunidadRequestDto): Response<OportunidadResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OportunidadResponseDto(1L, input.nombre, input.tipoOportunidad, input.descripcion, input.fuenteInformacion, input.fechaDeteccion, input.responsableInterno, input.prioridad, input.potencialEstimado, input.estado, input.fechaUltimaActualizacion, input.organizacionId, input.observacion))
    }
    override suspend fun findAll(): Response<List<OportunidadResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: Long): Response<OportunidadResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OportunidadResponseDto(id, "", "", "", "", "", "", "", "", "", ""))
    }
    override suspend fun update(id: Long, input: OportunidadUpdateDto): Response<OportunidadResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OportunidadResponseDto(id, input.nombre, input.tipoOportunidad, input.descripcion, input.fuenteInformacion, input.fechaDeteccion, input.responsableInterno, input.prioridad, input.potencialEstimado, input.estado, input.fechaUltimaActualizacion, input.organizacionId, input.observacion))
    }
    override suspend fun delete(id: Long): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}

class FakeOrganizacionApiService : OrganizacionApiService {
    var shouldFail = true
    override suspend fun create(input: OrganizacionRequestDto): Response<OrganizacionResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OrganizacionResponseDto(1L, input.nombre, input.tipoOrganizacion, input.rubro, input.comuna, input.region, input.website, input.audienciaObjetivo, input.observacion))
    }
    override suspend fun findAll(): Response<List<OrganizacionResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: Long): Response<OrganizacionResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OrganizacionResponseDto(id, "", "", "", "", "", null, null, null))
    }
    override suspend fun update(id: Long, input: OrganizacionUpdateDto): Response<OrganizacionResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(OrganizacionResponseDto(id, input.nombre, input.tipoOrganizacion, input.rubro, input.comuna, input.region, input.website, input.audienciaObjetivo, input.observacion))
    }
    override suspend fun delete(id: Long): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}

class FakeSeguimientoApiService : SeguimientoApiService {
    var shouldFail = true
    override suspend fun create(input: SeguimientoRequestDto): Response<SeguimientoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(SeguimientoResponseDto(1L, input.oportunidadId, input.contactoId, input.usuarioEmail, input.tipoGestion, input.propuesta, input.respuestaCliente, input.proximaAccion, input.fechaGestion, input.fechaProximaAccion, input.observacion))
    }
    override suspend fun findAll(): Response<List<SeguimientoResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: Long): Response<SeguimientoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(SeguimientoResponseDto(id, 1L, 1L, "", "", "", "", "", "", "", ""))
    }
    override suspend fun update(id: Long, input: SeguimientoUpdateDto): Response<SeguimientoResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(SeguimientoResponseDto(id, input.oportunidadId, input.contactoId, input.usuarioEmail, input.tipoGestion, input.propuesta, input.respuestaCliente, input.proximaAccion, input.fechaGestion, input.fechaProximaAccion, input.observacion))
    }
    override suspend fun delete(id: Long): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}

class FakeUsuarioApiService : UsuarioApiService {
    var shouldFail = true
    override suspend fun create(input: UsuarioRequestDto): Response<UsuarioResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(UsuarioResponseDto("1", input.pNombre, input.sNombre, input.pApellido, input.sApellido, input.fecNac, input.email, input.rol, input.fotoUri))
    }
    override suspend fun findAll(): Response<List<UsuarioResponseDto>> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(emptyList())
    }
    override suspend fun findById(id: String): Response<UsuarioResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(UsuarioResponseDto(id, "", "", "", "", 0, "", "", ""))
    }
    override suspend fun update(id: String, input: UsuarioUpdateDto): Response<UsuarioResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(UsuarioResponseDto(id, input.pNombre, input.sNombre, input.pApellido, input.sApellido, input.fecNac, "", input.rol, input.fotoUri))
    }
    override suspend fun updatePassword(id: String, input: UsuarioPasswordUpdateDto): Response<UsuarioResponseDto> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(UsuarioResponseDto(id, "", "", "", "", 0, "", "", ""))
    }
    override suspend fun delete(id: String): Response<Void> {
        if (shouldFail) throw IOException("Offline test mode")
        return Response.success(null)
    }
}
