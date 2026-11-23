package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.OportunidadRequestDto
import com.example.gestionmovil.data.remote.dto.OportunidadResponseDto
import com.example.gestionmovil.data.remote.dto.OportunidadUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OportunidadApiService {

    @POST("api/oportunidades")
    suspend fun create(@Body input: OportunidadRequestDto): Response<OportunidadResponseDto>

    @GET("api/oportunidades")
    suspend fun findAll(): Response<List<OportunidadResponseDto>>

    @GET("api/oportunidades/{id}")
    suspend fun findById(@Path("id") id: Long): Response<OportunidadResponseDto>

    @PUT("api/oportunidades/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body input: OportunidadUpdateDto
    ): Response<OportunidadResponseDto>

    @DELETE("api/oportunidades/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Void>
}

