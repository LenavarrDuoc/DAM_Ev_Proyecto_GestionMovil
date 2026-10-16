package com.example.gestionmovil.data

import com.example.gestionmovil.data.UsuarioDao
import com.example.gestionmovil.models.Usuario
import com.example.gestionmovil.utils.Seguridad

class UsuarioRepository (private val usuarioDao: UsuarioDao){
    /**
     * Autentica al usuario en el Login
     */
    suspend fun loginUsuario(email: String, passwordIngresada: String): Result<Usuario> {
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
     * Guarda un nuevo usuario en la base de datos (Útil para la pantalla de Registro)
     */
    suspend fun registrarUsuario(usuario: Usuario): Result<Boolean> {
        return try {
            // Verificar si el correo ya existe
            val existente = usuarioDao.obtenerPorEmail(usuario.email)
            if (existente != null) {
                return Result.failure(Exception("El correo ya se encuentra registrado"))
            }

            // Hashear la contraseña ANTES de guardar en SQLite
            val usuarioConHash = usuario.copy(
                password = Seguridad.hashPassword(usuario.password)
            )

            usuarioDao.insertarUsuario(usuarioConHash)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}