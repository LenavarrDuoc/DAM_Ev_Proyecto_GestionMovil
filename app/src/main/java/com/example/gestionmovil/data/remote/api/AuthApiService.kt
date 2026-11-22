package com.example.gestionmovil.data.remote.api

import com.example.gestionmovil.data.remote.dto.LoginRequestDto
import com.example.gestionmovil.data.remote.dto.UsuarioResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<UsuarioResponseDto>

}