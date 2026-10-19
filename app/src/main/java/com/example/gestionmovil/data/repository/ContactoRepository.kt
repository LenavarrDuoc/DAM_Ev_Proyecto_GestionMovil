package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.ContactoDao
import com.example.gestionmovil.data.local.entities.ContactoEntity
import kotlinx.coroutines.flow.Flow

class ContactoRepository(private val contactoDao: ContactoDao) {

    val todosLosContactos: Flow<List<ContactoEntity>> = contactoDao.obtenerTodosLosContactos()

    suspend fun insertarContacto(contacto: ContactoEntity): Long {
        return contactoDao.insertarContacto(contacto)
    }

    suspend fun actualizarContacto(contacto: ContactoEntity) {
        contactoDao.actualizarContacto(contacto)
    }

    suspend fun eliminarContacto(contacto: ContactoEntity) {
        contactoDao.eliminarContacto(contacto)
    }

    suspend fun obtenerContactoPorId(id: Long): ContactoEntity? {
        return contactoDao.obtenerContactoPorId(id)
    }

    fun obtenerContactosPorOrganizacion(orgId: Long): Flow<List<ContactoEntity>> {
        return contactoDao.obtenerContactosPorOrganizacion(orgId)
    }
}