package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.UsuarioPasswordUpdateDto
import com.example.gestionmovil.data.remote.dto.UsuarioRequestDto
import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import com.example.gestionmovil.data.remote.dto.UsuarioUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApiService {

    @POST("api/usuarios")
    suspend fun create(@Body input: UsuarioRequestDto): Response<UsuarioResponseDto>

    @GET("api/usuarios")
    suspend fun findAll(): Response<List<UsuarioResponseDto>>

    @GET("api/usuarios/{id}")
    suspend fun findById(@Path("id") id: String): Response<UsuarioResponseDto>

    @PUT("api/usuarios/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body input: UsuarioUpdateDto
    ): Response<UsuarioResponseDto>

    // NUEVO: Endpoint PATCH para actualizar solo la contraseña
    @PATCH("api/usuarios/{id}/password")
    suspend fun updatePassword(
        @Path("id") id: String,
        @Body input: UsuarioPasswordUpdateDto
    ): Response<UsuarioResponseDto>

    @DELETE("api/usuarios/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}