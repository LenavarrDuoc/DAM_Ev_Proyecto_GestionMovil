package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.ContactoRequestDto
import com.example.gestionmovil.data.remote.dto.ContactoResponseDto
import com.example.gestionmovil.data.remote.dto.ContactoUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactoApiService {

    @POST("api/contactos")
    suspend fun create(@Body input: ContactoRequestDto): Response<ContactoResponseDto>

    @GET("api/contactos")
    suspend fun findAll(): Response<List<ContactoResponseDto>>

    @GET("api/contactos/{id}")
    suspend fun findById(@Path("id") id: Long): Response<ContactoResponseDto>

    @PUT("api/contactos/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body input: ContactoUpdateDto
    ): Response<ContactoResponseDto>

    @DELETE("api/contactos/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Void>
}