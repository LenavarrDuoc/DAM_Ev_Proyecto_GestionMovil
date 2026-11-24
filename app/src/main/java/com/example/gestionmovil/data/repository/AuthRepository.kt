package com.example.gestionmovil.data.repository

import com.example.gestionmovil.data.local.dao.UsuarioDao
import com.example.gestionmovil.data.local.entities.UsuarioEntity
import com.example.gestionmovil.data.remote.api.AuthApiService
import com.example.gestionmovil.data.remote.client.RetrofitClient
import com.example.gestionmovil.data.remote.dto.LoginRequestDto
import com.example.gestionmovil.data.remote.mapper.toEntity
import com.example.gestionmovil.utils.Seguridad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val authApi: AuthApiService = RetrofitClient.authApi,
    private val usuarioDao: UsuarioDao
) {
    /**
     * Realiza el login usando Result de Kotlin para manejar éxitos y errores fácilmente.
     * Hashea la contraseña con SHA-256 antes de enviarla para coincidir con el registro.
     */
    suspend fun login(email: String, pass: String): Result<UsuarioEntity> {
        return withContext(Dispatchers.IO) {
            val hashPassword = Seguridad.hashPassword(pass)

            try {
                // 1. Intentamos el login remoto en el Backend enviando la contraseña hasheada
                val request = LoginRequestDto(email = email, password = hashPassword)
                val response = authApi.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // 2. Mapeamos la respuesta a una Entidad de Room
                        val usuarioEntity = body.toEntity()

                        // 3. Guardamos el hash localmente para permitir login offline en el futuro
                        val usuarioParaGuardar = usuarioEntity.copy(
                            password = hashPassword
                        )

                        // 4. Guardamos o actualizamos en Room
                        usuarioDao.insertarUsuarios(listOf(usuarioParaGuardar))

                        Result.success(usuarioParaGuardar)
                    } else {
                        Result.failure(Exception("El servidor respondió correctamente, pero sin datos."))
                    }
                } else {
                    // El servidor devolvió un error (ej: 401 Credenciales inválidas, 404 No encontrado)
                    Result.failure(Exception("Credenciales inválidas o error en el servidor."))
                }

            } catch (e: Exception) {
                // 5. CAÍDA DE RED (Exception de Retrofit/OkHttp al no haber internet)
                // Intentamos un Login Local de contingencia (Offline)
                try {
                    val usuarioLocal = usuarioDao.obtenerPorEmail(email)

                    if (usuarioLocal != null) {
                        if (usuarioLocal.password == hashPassword) {
                            // Login offline exitoso
                            Result.success(usuarioLocal)
                        } else {
                            Result.failure(Exception("Contraseña incorrecta"))
                        }
                    } else {
                        Result.failure(Exception("El correo no está registrado"))
                    }
                } catch (localException: Exception) {
                    Result.failure(Exception("Error crítico de conexión: ${e.localizedMessage}"))
                }
            }
        }
    }
}