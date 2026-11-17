package com.example.gestionmovil.fakes

import com.example.gestionmovil.data.local.dao.*
import com.example.gestionmovil.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUsuarioDao : UsuarioDao {
    val usuarios = mutableListOf<UsuarioEntity>()
    private val usuariosFlow = MutableStateFlow<List<UsuarioEntity>>(emptyList())

    private fun notifyChanges() {
        usuariosFlow.value = usuarios.toList()
    }

    override suspend fun obtenerPorEmail(email: String): UsuarioEntity? {
        return usuarios.find { it.email.equals(email, ignoreCase = true) }
    }

    override suspend fun insertarUsuario(usuarioEntity: UsuarioEntity) {
        usuarios.removeAll { it.id == usuarioEntity.id }
        usuarios.add(usuarioEntity)
        notifyChanges()
    }

    override suspend fun obtenerPorId(id: String): UsuarioEntity? {
        return usuarios.find { it.id == id }
    }

    override suspend fun login(email: String, password: String): UsuarioEntity? {
        return usuarios.find { it.email.equals(email, ignoreCase = true) && it.password == password }
    }

    override fun obtenerEjecutivos(): Flow<List<UsuarioEntity>> {
        return usuariosFlow.map { list -> list.filter { it.rol == "EJECUTIVO" } }
    }

    override suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>) {
        this.usuarios.addAll(usuarios)
        notifyChanges()
    }

    override suspend fun actualizarFotoPerfil(id: String, fotoUri: String?) {
        val index = usuarios.indexOfFirst { it.id == id }
        if (index != -1) {
            usuarios[index] = usuarios[index].copy(fotoUri = fotoUri)
            notifyChanges()
        }
    }
}

class FakeOportunidadDao : OportunidadDao {
    val oportunidades = mutableListOf<OportunidadEntity>()
    val organizaciones = mutableListOf<OrganizacionEntity>()
    private val oportunidadesFlow = MutableStateFlow<List<OportunidadEntity>>(emptyList())
    private var nextId = 1L

    private fun notifyChanges() {
        oportunidadesFlow.value = oportunidades.toList()
    }

    override fun getOportunidadesConOrganizacion(): Flow<List<OportunidadConOrganizacion>> {
        return oportunidadesFlow.map { list ->
            list.map { op ->
                val org = organizaciones.find { it.id == op.organizacionId }
                OportunidadConOrganizacion(oportunidad = op, organizacion = org)
            }
        }
    }

    override suspend fun insertOportunidad(oportunidad: OportunidadEntity): Long {
        val id = if (oportunidad.id == 0L) nextId++ else oportunidad.id
        val newOp = oportunidad.copy(id = id)
        oportunidades.removeAll { it.id == id }
        oportunidades.add(newOp)
        notifyChanges()
        return id
    }

    override suspend fun updateOportunidad(oportunidad: OportunidadEntity) {
        val index = oportunidades.indexOfFirst { it.id == oportunidad.id }
        if (index != -1) {
            oportunidades[index] = oportunidad
            notifyChanges()
        }
    }

    override fun getAllOportunidades(): Flow<List<OportunidadEntity>> {
        return oportunidadesFlow.map { list -> list.sortedByDescending { it.id } }
    }

    override suspend fun getOportunidadById(opId: Long): OportunidadEntity? {
        return oportunidades.find { it.id == opId }
    }

    override suspend fun updateEstado(estId: Long, nuevoEstado: String, fechaActualizacion: String) {
        val index = oportunidades.indexOfFirst { it.id == estId }
        if (index != -1) {
            oportunidades[index] = oportunidades[index].copy(
                estado = nuevoEstado,
                fechaUltimaActualizacion = fechaActualizacion
            )
            notifyChanges()
        }
    }

    override fun getCantidadOportunidades(): Flow<Int> {
        return oportunidadesFlow.map { it.size }
    }
}

class FakeOrganizacionDao : OrganizacionDao {
    val organizaciones = mutableListOf<OrganizacionEntity>()
    val contactos = mutableListOf<ContactoEntity>()
    private val organizacionesFlow = MutableStateFlow<List<OrganizacionEntity>>(emptyList())
    private val contactosFlow = MutableStateFlow<List<ContactoEntity>>(emptyList())
    private var nextOrgId = 1L

    private fun notifyOrgChanges() { organizacionesFlow.value = organizaciones.toList() }
    private fun notifyContactoChanges() { contactosFlow.value = contactos.toList() }

    override suspend fun insertOrganizacion(organizacion: OrganizacionEntity): Long {
        val id = if (organizacion.id == 0L) nextOrgId++ else organizacion.id
        val entity = organizacion.copy(id = id)
        organizaciones.removeAll { it.id == id }
        organizaciones.add(entity)
        notifyOrgChanges()
        return id
    }

    override suspend fun updateOrganizacion(organizacion: OrganizacionEntity) {
        val index = organizaciones.indexOfFirst { it.id == organizacion.id }
        if (index != -1) {
            organizaciones[index] = organizacion
            notifyOrgChanges()
        }
    }

    override fun getAllOrganizaciones(): Flow<List<OrganizacionEntity>> {
        return organizacionesFlow.map { list -> list.sortedBy { it.nombre } }
    }

    override suspend fun getOrganizacionById(orgId: Long): OrganizacionEntity? {
        return organizaciones.find { it.id == orgId }
    }

    override suspend fun insertContacto(contacto: ContactoEntity) {
        contactos.removeAll { it.id == contacto.id && contacto.id != 0L }
        contactos.add(contacto)
        notifyContactoChanges()
    }

    override suspend fun updateContacto(contacto: ContactoEntity) {
        val index = contactos.indexOfFirst { it.id == contacto.id }
        if (index != -1) {
            contactos[index] = contacto
            notifyContactoChanges()
        }
    }

    override fun getContactosByOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return contactosFlow.map { list -> list.filter { it.organizacionId == orgId } }
    }
}

class FakeContactoDao : ContactoDao {
    val contactos = mutableListOf<ContactoEntity>()
    private val contactosFlow = MutableStateFlow<List<ContactoEntity>>(emptyList())
    private var nextId = 1L

    private fun notifyChanges() { contactosFlow.value = contactos.toList() }

    override suspend fun insertarContacto(contacto: ContactoEntity): Long {
        val id = if (contacto.id == 0L) nextId++ else contacto.id
        val entity = contacto.copy(id = id)
        contactos.removeAll { it.id == id }
        contactos.add(entity)
        notifyChanges()
        return id
    }

    override suspend fun insertarContactos(contactos: List<ContactoEntity>) {
        this.contactos.addAll(contactos)
        notifyChanges()
    }

    override suspend fun actualizarContacto(contacto: ContactoEntity) {
        val index = contactos.indexOfFirst { it.id == contacto.id }
        if (index != -1) {
            contactos[index] = contacto
            notifyChanges()
        }
    }

    override suspend fun eliminarContacto(contacto: ContactoEntity) {
        contactos.removeAll { it.id == contacto.id }
        notifyChanges()
    }

    override fun obtenerTodosLosContactos(): Flow<List<ContactoEntity>> {
        return contactosFlow.map { list -> list.sortedBy { it.nombre } }
    }

    override suspend fun obtenerContactoPorId(contactoId: Long): ContactoEntity? {
        return contactos.find { it.id == contactoId }
    }

    override fun obtenerContactosPorOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return contactosFlow.map { list -> list.filter { it.organizacionId == orgId }.sortedBy { it.nombre } }
    }
}

class FakeEventoDao : EventoDao {
    val eventos = mutableListOf<EventoEntity>()
    private val eventosFlow = MutableStateFlow<List<EventoEntity>>(emptyList())

    private fun notifyChanges() { eventosFlow.value = eventos.toList() }

    override suspend fun insertarEvento(evento: EventoEntity) {
        eventos.add(evento)
        notifyChanges()
    }

    override fun obtenerTodosLosEventos(): Flow<List<EventoEntity>> {
        return eventosFlow.map { list -> list.sortedByDescending { it.fecha } }
    }
}

class FakeSeguimientoDao : SeguimientoDao {
    val seguimientos = mutableListOf<SeguimientoEntity>()
    private val seguimientosFlow = MutableStateFlow<List<SeguimientoEntity>>(emptyList())
    private var nextId = 1L

    private fun notifyChanges() { seguimientosFlow.value = seguimientos.toList() }

    override suspend fun insertarSeguimiento(seguimiento: SeguimientoEntity): Long {
        val id = if (seguimiento.id == 0L) nextId++ else seguimiento.id
        val entity = seguimiento.copy(id = id)
        seguimientos.removeAll { it.id == id }
        seguimientos.add(entity)
        notifyChanges()
        return id
    }

    override suspend fun actualizarSeguimiento(seguimiento: SeguimientoEntity) {
        val index = seguimientos.indexOfFirst { it.id == seguimiento.id }
        if (index != -1) {
            seguimientos[index] = seguimiento
            notifyChanges()
        }
    }

    override suspend fun eliminarSeguimiento(seguimiento: SeguimientoEntity) {
        seguimientos.removeAll { it.id == seguimiento.id }
        notifyChanges()
    }

    override fun obtenerSeguimientosPorOportunidad(oportunidadId: Long): Flow<List<SeguimientoEntity>> {
        return seguimientosFlow.map { list -> list.filter { it.oportunidadId == oportunidadId }.sortedByDescending { it.id } }
    }

    override suspend fun obtenerSeguimientoPorId(id: Long): SeguimientoEntity? {
        return seguimientos.find { it.id == id }
    }

    override fun obtenerProximasAccionesPendientes(): Flow<List<SeguimientoEntity>> {
        return seguimientosFlow.map { list -> list.filter { it.proximaAccion.isNotBlank() }.sortedBy { it.fechaProximaAccion } }
    }
}
