package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.UsuarioDao
import com.example.gestionmovil.data.local.entities.RolUsuarioEntity
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.remote.api.UsuarioApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.dto.UsuarioPasswordUpdateDto
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.data.remote.mapper.toEntityList
import com.example.gestionmovil.data.remote.mapper.toRequestDto
import com.example.gestionmovil.data.remote.mapper.toUpdateDto
import com.example.gestionmovil.utils.Seguridad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val usuarioApi: UsuarioApiService = RetrofitClient.usuarioApi
) {

    /**
     * Guarda un nuevo usuario en el Backend y luego lo persiste en Room.
     * Fuerza el rol a CLIENTE y hashea la contraseña (SHA-256) antes de enviarla.
     */
    suspend fun registrarUsuario(usuarioEntity: UsuarioEntity): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Verificar localmente si el correo ya existe para evitar llamadas innecesarias
                val existenteLocal = usuarioDao.obtenerPorEmail(usuarioEntity.email)
                if (existenteLocal != null) {
                    return@withContext Result.failure(Exception("El correo ya se encuentra registrado"))
                }

                // 2. Aplicar reglas de negocio: hash SHA-256 y rol CLIENTE
                val passwordHasheada = Seguridad.hashPassword(usuarioEntity.password)
                val usuarioPreparado = usuarioEntity.copy(
                    password = passwordHasheada,
                    rol = RolUsuarioEntity.CLIENTE.name
                )

                // 3. Construir el DTO de Request inyectando la contraseña hasheada
                val requestDto = usuarioPreparado.toRequestDto().copy(
                    password = passwordHasheada
                )

                // 4. Enviar al Backend (POST /api/usuarios)
                val response = usuarioApi.create(requestDto)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // 5. Guardar en Room usando el ID oficial asignado por el servidor
                        // pero conservando el hash de la contraseña localmente para soporte offline
                        val entidadSincronizada = body.toEntity().copy(
                            password = passwordHasheada
                        )
                        usuarioDao.insertarUsuario(entidadSincronizada)
                        Result.success(true)
                    } else {
                        Result.failure(Exception("El servidor registró el usuario pero no devolvió datos."))
                    }
                } else {
                    val errorMsg = if (response.code() == 400 || response.code() == 409) {
                        "El correo ya se encuentra registrado o los datos son inválidos"
                    } else {
                        "Error del servidor al registrar (${response.code()})"
                    }
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Sin conexión a internet. Se requiere red para registrar una cuenta nueva."))
            }
        }
    }

    /**
     * Sincroniza la lista completa de usuarios desde el Backend hacia Room.
     */
    suspend fun sincronizarUsuarios(): Result<List<UsuarioEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = usuarioApi.findAll()
                if (response.isSuccessful && response.body() != null) {
                    val entidades = response.body()!!.toEntityList()
                    usuarioDao.insertarUsuarios(entidades)
                    Result.success(entidades)
                } else {
                    Result.failure(Exception("Error al obtener usuarios (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Actualiza los datos de perfil de un usuario en Backend y Room.
     */
    suspend fun actualizarUsuario(usuarioEntity: UsuarioEntity): Result<UsuarioEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = usuarioApi.update(usuarioEntity.id, usuarioEntity.toUpdateDto())
                if (response.isSuccessful && response.body() != null) {
                    // Conservamos la contraseña local ya que ResponseDTO no la incluye
                    val actualizado = response.body()!!.toEntity().copy(
                        password = usuarioEntity.password
                    )
                    usuarioDao.insertarUsuario(actualizado)
                    Result.success(actualizado)
                } else {
                    Result.failure(Exception("No se pudo actualizar el perfil (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Actualiza únicamente la contraseña (PATCH /api/usuarios/{id}/password)
     */
    suspend fun actualizarPassword(usuarioId: String, nuevaPasswordPlana: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val hashNuevo = Seguridad.hashPassword(nuevaPasswordPlana)
                val dto = UsuarioPasswordUpdateDto(password = hashNuevo)
                val response = usuarioApi.updatePassword(usuarioId, dto)

                if (response.isSuccessful && response.body() != null) {
                    val actualizado = response.body()!!.toEntity().copy(password = hashNuevo)
                    usuarioDao.insertarUsuario(actualizado)
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error al cambiar la contraseña (${response.code()})"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}