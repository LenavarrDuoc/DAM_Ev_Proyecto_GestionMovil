package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.SeguimientoRequestDto
import com.example.gestionmovil.data.remote.dto.SeguimientoResponseDto
import com.example.gestionmovil.data.remote.dto.SeguimientoUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SeguimientoApiService {

    @POST("api/seguimientos")
    suspend fun create(@Body input: SeguimientoRequestDto): Response<SeguimientoResponseDto>

    @GET("api/seguimientos")
    suspend fun findAll(): Response<List<SeguimientoResponseDto>>

    @GET("api/seguimientos/{id}")
    suspend fun findById(@Path("id") id: Long): Response<SeguimientoResponseDto>

    @PUT("api/seguimientos/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body input: SeguimientoUpdateDto
    ): Response<SeguimientoResponseDto>

    @DELETE("api/seguimientos/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Void>
}