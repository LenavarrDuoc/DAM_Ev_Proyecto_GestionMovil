package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import kotlinx.coroutines.flow.Flow

class OrganizacionRepository(
    private val organizacionDao: OrganizacionDao
) {
    // ==========================================
    // OPERACIONES DE ORGANIZACIONES
    // ==========================================

    /**
     * Obtiene el listado de todas las organizaciones registradas.
     */
    fun getAllOrganizaciones(): Flow<List<OrganizacionEntity>> {
        return organizacionDao.getAllOrganizaciones()
    }

    /**
     * Busca una organización por su identificador.
     */
    suspend fun getOrganizacionById(id: Long): OrganizacionEntity? {
        return organizacionDao.getOrganizacionById(id)
    }

    /**
     * Registra una nueva organización en el sistema.
     * Retorna el ID autogenerado.
     */
    suspend fun insertOrganizacion(organizacion: OrganizacionEntity): Long {
        return organizacionDao.insertOrganizacion(organizacion)
    }

    /**
     * Modifica los antecedentes de una organización.
     */
    suspend fun updateOrganizacion(organizacion: OrganizacionEntity) {
        organizacionDao.updateOrganizacion(organizacion)
    }

    // ==========================================
    // OPERACIONES DE CONTACTOS CORPORATIVOS
    // ==========================================

    /**
     * Obtiene los contactos ficticios asociados a una organización específica.
     */
    fun getContactosByOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return organizacionDao.getContactosByOrganizacion(orgId)
    }

    /**
     * Registra un nuevo contacto ficticio asociado a una organización.
     */
    suspend fun insertContacto(contacto: ContactoEntity) {
        organizacionDao.insertContacto(contacto)
    }

    /**
     * Actualiza la información de un contacto ficticio.
     */
    suspend fun updateContacto(contacto: ContactoEntity) {
        organizacionDao.updateContacto(contacto)
    }
}