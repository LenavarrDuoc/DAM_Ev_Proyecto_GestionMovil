package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.UsuarioDao
import com.example.gestionmovil.data.local.entities.RolUsuarioEntity
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.utils.Seguridad

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * Autentica al usuario en el Login
     */
    suspend fun loginUsuario(email: String, passwordIngresada: String): Result<UsuarioEntity> {
        return try {
            // 1. Buscar si el usuario existe en SQLite por correo
            val usuarioEncontrado = usuarioDao.obtenerPorEmail(email)
                ?: return Result.failure(Exception("El correo no está registrado"))

            // 2. Hashear la contraseña ingresada en el formulario
            val hashIngresado = Seguridad.hashPassword(passwordIngresada)

            // 3. Comparar el hash generado con el guardado en la BD
            if (usuarioEncontrado.password == hashIngresado) {
                Result.success(usuarioEncontrado)
            } else {
                Result.failure(Exception("Contraseña incorrecta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Guarda un nuevo usuario en la base de datos (Pantalla de Registro)
     * Forzando el rol a CLIENTE por regla de negocio.
     */
    suspend fun registrarUsuario(usuarioEntity: UsuarioEntity): Result<Boolean> {
        return try {
            // Verificar si el correo ya existe
            val existente = usuarioDao.obtenerPorEmail(usuarioEntity.email)
            if (existente != null) {
                return Result.failure(Exception("El correo ya se encuentra registrado"))
            }

            // Hashear la contraseña y forzar el rol a CLIENTE
            val usuarioConHash = usuarioEntity.copy(
                password = Seguridad.hashPassword(usuarioEntity.password),
                rol = RolUsuarioEntity.CLIENTE.name
            )

            usuarioDao.insertarUsuario(usuarioConHash)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}