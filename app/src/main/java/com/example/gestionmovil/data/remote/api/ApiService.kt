package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.LoginRequestDto
import com.example.gestionmovil.data.remote.dto.OportunidadRequestDto
import com.example.gestionmovil.data.remote.dto.OportunidadResponseDto
import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<UsuarioResponseDto>

    @GET("api/oportunidades")
    suspend fun getOportunidades(): Response<List<OportunidadResponseDto>>

    @POST("api/oportunidades")
    suspend fun crearOportunidad(@Body dto: OportunidadRequestDto): Response<OportunidadResponseDto>
}