package com.example.gestionmovil.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestionmovil.data.local.entities.ContactoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContacto(contacto: ContactoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContactos(contactos: List<ContactoEntity>)

    @Update
    suspend fun actualizarContacto(contacto: ContactoEntity)

    @Delete
    suspend fun eliminarContacto(contacto: ContactoEntity)

    @Query("SELECT * FROM contactos ORDER BY nombre ASC")
    fun obtenerTodosLosContactos(): Flow<List<ContactoEntity>>

    @Query("SELECT * FROM contactos WHERE id = :contactoId")
    suspend fun obtenerContactoPorId(contactoId: Long): ContactoEntity?

    @Query("SELECT * FROM contactos WHERE organizacionId = :orgId ORDER BY nombre ASC")
    fun obtenerContactosPorOrganizacion(orgId: Long): Flow<List<ContactoEntity>>
}