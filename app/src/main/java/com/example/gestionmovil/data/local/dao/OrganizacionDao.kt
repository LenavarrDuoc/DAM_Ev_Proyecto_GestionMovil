package com.example.gestionmovil.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizacion(organizacion: OrganizacionEntity): Long

    @Update
    suspend fun updateOrganizacion(organizacion: OrganizacionEntity)

    @Query("SELECT * FROM organizaciones ORDER BY name ASC")
    fun getAllOrganizaciones(): Flow<List<OrganizacionEntity>>

    @Query("SELECT * FROM organizaciones WHERE id = :orgId")
    suspend fun getOrganizacionById(orgId: Long): OrganizacionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacto(contacto: ContactoEntity)

    @Update
    suspend fun updateContacto(contacto: ContactoEntity)

    @Query("SELECT * FROM contactos WHERE organizacionId = :orgId")
    fun getContactosByOrganizacion(orgId: Long): Flow<List<ContactoEntity>>
}