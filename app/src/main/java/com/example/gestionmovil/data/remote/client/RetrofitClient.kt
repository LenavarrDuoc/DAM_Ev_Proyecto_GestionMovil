package com.example.gestionmovil.data.remote.client

import com.example.gestionmovil.data.remote.api.AuthApiService
import com.example.gestionmovil.data.remote.api.ContactoApiService
import com.example.gestionmovil.data.remote.api.EventoApiService
import com.example.gestionmovil.data.remote.api.OportunidadApiService
import com.example.gestionmovil.data.remote.api.OrganizacionApiService
import com.example.gestionmovil.data.remote.api.SeguimientoApiService
import com.example.gestionmovil.data.remote.api.UsuarioApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Ajustar según entorno

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Exponemos las interfaces por separado
    val authApi: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    // API de Organizaciones
    val oportunidadApi: OportunidadApiService by lazy { retrofit.create(OportunidadApiService::class.java) }
    // API de Oportunidades
    val organizacionApi: OrganizacionApiService by lazy {
        retrofit.create(OrganizacionApiService::class.java)
    }
    // API de Contactos
    val contactoApi: ContactoApiService by lazy {
        retrofit.create(ContactoApiService::class.java)
    }
    // API de Eventos
    val eventoApi: EventoApiService by lazy {
        retrofit.create(EventoApiService::class.java)
    }
    // API de Seguimientos
    val seguimientoApi: SeguimientoApiService by lazy {
        retrofit.create(SeguimientoApiService::class.java)
    }

    // API de Usuarios
    val usuarioApi: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

}