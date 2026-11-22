package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.OrganizacionRequestDto
import com.example.gestionmovil.data.remote.dto.OrganizacionResponseDto
import com.example.gestionmovil.data.remote.dto.OrganizacionUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrganizacionApiService {

    @POST("api/organizaciones")
    suspend fun create(@Body input: OrganizacionRequestDto): Response<OrganizacionResponseDto>

    @GET("api/organizaciones")
    suspend fun findAll(): Response<List<OrganizacionResponseDto>>

    @GET("api/organizaciones/{id}")
    suspend fun findById(@Path("id") id: Long): Response<OrganizacionResponseDto>

    @PUT("api/organizaciones/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body input: OrganizacionUpdateDto
    ): Response<OrganizacionResponseDto>

    @DELETE("api/organizaciones/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Void>
}