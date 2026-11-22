package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.EventoRequestDto
import com.example.gestionmovil.data.remote.dto.EventoResponseDto
import com.example.gestionmovil.data.remote.dto.EventoUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventoApiService {

    @POST("api/eventos")
    suspend fun create(@Body input: EventoRequestDto): Response<EventoResponseDto>

    @GET("api/eventos")
    suspend fun findAll(): Response<List<EventoResponseDto>>

    @GET("api/eventos/{id}")
    suspend fun findById(@Path("id") id: String): Response<EventoResponseDto>

    @PUT("api/eventos/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body input: EventoUpdateDto
    ): Response<EventoResponseDto>

    @DELETE("api/eventos/{id}")
    suspend fun delete(@Path("id") id: String): Response<Void>
}