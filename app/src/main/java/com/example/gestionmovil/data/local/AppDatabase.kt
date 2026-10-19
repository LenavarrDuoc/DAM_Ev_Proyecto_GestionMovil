package com.example.gestionmovil.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection

// DAOs
import com.example.gestionmovil.data.local.dao.ContactoDao
import com.example.gestionmovil.data.local.dao.EventoDao
import com.example.gestionmovil.data.local.dao.OportunidadDao
import com.example.gestionmovil.data.local.dao.OrganizacionDao
import com.example.gestionmovil.data.local.dao.SeguimientoDao
import com.example.gestionmovil.data.local.dao.UsuarioDao

// Entidades
import com.example.gestionmovil.data.local.entities.ContactoEntity
import com.example.gestionmovil.data.local.entities.EventoEntity
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.local.entities.OrganizacionEntity
import com.example.gestionmovil.data.local.entities.RolUsuarioEntity
import com.example.gestionmovil.data.local.entities.SeguimientoEntity
import com.example.gestionmovil.data.local.entities.UsuarioEntity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Database(
    entities = [
        OrganizacionEntity::class,
        ContactoEntity::class,
        OportunidadEntity::class,
        SeguimientoEntity::class,
        UsuarioEntity::class,
        EventoEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun seguimientoDao(): SeguimientoDao
    abstract fun oportunidadDao(): OportunidadDao
    abstract fun organizacionDao(): OrganizacionDao
    abstract fun eventoDao(): EventoDao
    abstract fun contactoDao(): ContactoDao // <-- Método abstracto agregado

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_movil_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback : RoomDatabase.Callback() {
            override suspend fun onOpen(connection: SQLiteConnection) {
                super.onOpen(connection)

                CoroutineScope(Dispatchers.IO).launch {
                    var db = INSTANCE
                    var retries = 0
                    while (db == null && retries < 10) {
                        kotlinx.coroutines.delay(100)
                        db = INSTANCE
                        retries++
                    }

                    db?.let { database ->
                        // 1. Cuentas
                        val usuarioDao = database.usuarioDao()
                        if (usuarioDao.obtenerPorEmail("admin@empresa.cl") == null) {
                            poblarCuentasIniciales(usuarioDao)
                        }

                        // 2. Eventos
                        val eventoDao = database.eventoDao()
                        if (eventoDao.obtenerTodosLosEventos().firstOrNull().isNullOrEmpty()) {
                            poblarEventosIniciales(eventoDao)
                        }

                        // 3. Oportunidades
                        val oportunidadDao = database.oportunidadDao()
                        if (oportunidadDao.getAllOportunidades().firstOrNull().isNullOrEmpty()) {
                            poblarOportunidadesIniciales(oportunidadDao)
                        }
                    }
                }
            }

            suspend fun poblarCuentasIniciales(usuarioDao: UsuarioDao) {
                val passwordHasheada = com.example.gestionmovil.utils.Seguridad.hashPassword("Clave123+")
                val cuentasPrueba = listOf(
                    UsuarioEntity(
                        pNombre = "Admin",
                        pApellido = "Sistema",
                        email = "admin@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.ADMIN.name
                    ),
                    UsuarioEntity(
                        pNombre = "Carlos",
                        pApellido = "Mendoza",
                        email = "supervisor@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.SUPERVISOR.name
                    ),
                    UsuarioEntity(
                        pNombre = "Ana",
                        pApellido = "López",
                        email = "ejecutivo1@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    ),
                    UsuarioEntity(
                        pNombre = "Mateo",
                        pApellido = "Silva",
                        email = "ejecutivo2@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    ),
                    UsuarioEntity(
                        pNombre = "Sofía",
                        pApellido = "Rojas",
                        email = "ejecutivo3@empresa.cl",
                        password = passwordHasheada,
                        rol = RolUsuarioEntity.EJECUTIVO.name
                    )
                )
                usuarioDao.insertarUsuarios(cuentasPrueba)
            }

            suspend fun poblarEventosIniciales(eventoDao: EventoDao) {
                val eventosPrueba = listOf(
                    EventoEntity(
                        titulo = "Feria Tecnológica e Innovación Regional",
                        tipoEvento = "Feria",
                        fecha = "2026-11-15",
                        hora = "09:00",
                        region = "Región Metropolitana",
                        comuna = "Santiago",
                        organizacionId = "ORG-001",
                        organizacionNombre = "Gobierno Regional Metropolitano",
                        publicoEsperado = 500,
                        condicionesParticipacion = "Entrada liberada previo registro en el portal corporativo."
                    ),
                    EventoEntity(
                        titulo = "Seminario de Transformación Digital CRM",
                        tipoEvento = "Seminario",
                        fecha = "2026-11-20",
                        hora = "14:30",
                        region = "Valparaíso",
                        comuna = "Viña del Mar",
                        organizacionId = "ORG-002",
                        organizacionNombre = "Cámara de Comercio Valparaíso",
                        publicoEsperado = 120,
                        condicionesParticipacion = "Exclusivo para ejecutivos y supervisores de área."
                    ),
                    EventoEntity(
                        titulo = "Conferencia Anual de Ciberseguridad",
                        tipoEvento = "Conferencia",
                        fecha = "2026-12-05",
                        hora = "10:00",
                        region = "Biobío",
                        comuna = "Concepción",
                        organizacionId = "ORG-003",
                        organizacionNombre = "Universidad del Biobío",
                        publicoEsperado = 300,
                        condicionesParticipacion = "Presentación de credencial institucional al ingreso."
                    )
                )

                eventosPrueba.forEach { evento ->
                    eventoDao.insertarEvento(evento)
                }
            }

            suspend fun poblarOportunidadesIniciales(oportunidadDao: OportunidadDao) {
                val oportunidadesPrueba = listOf(
                    OportunidadEntity(
                        nombre = "Licencia CRM Corporativo 2026",
                        tipoOportunidad = "Licitación Pública",
                        descripcion = "Implementación de licencias CRM y módulo móvil para el equipo comercial de zona central.",
                        fuenteInformacion = "Mercado Público",
                        fechaDeteccion = "2026-10-01",
                        responsableInterno = "Ana López",
                        prioridad = "Alta",
                        potencialEstimado = "Alto",
                        estado = "Contacto realizado",
                        fechaUltimaActualizacion = "2026-10-15",
                        observacion = "Cliente solicitó reunión técnica para revisar términos de soporte."
                    ),
                    OportunidadEntity(
                        nombre = "Renovación Equipos de Cómputo",
                        tipoOportunidad = "Venta Directa",
                        descripcion = "Renovación de 50 laptops corporativas para gerencia y áreas operativas.",
                        fuenteInformacion = "Contacto Directo",
                        fechaDeteccion = "2026-10-05",
                        responsableInterno = "Mateo Silva",
                        prioridad = "Media",
                        potencialEstimado = "Medio",
                        estado = "Propuesta enviada",
                        fechaUltimaActualizacion = "2026-10-18",
                        observacion = "Propuesta comercial enviada a departamento de finanzas."
                    ),
                    OportunidadEntity(
                        nombre = "Auditoría e Infraestructura Cloud",
                        tipoOportunidad = "Consultoría",
                        descripcion = "Evaluación de seguridad y optimización de costos en infraestructura AWS/Azure.",
                        fuenteInformacion = "Recomendación",
                        fechaDeteccion = "2026-10-10",
                        responsableInterno = "Sofía Rojas",
                        prioridad = "Baja",
                        potencialEstimado = "Bajo",
                        estado = "Evento identificado",
                        fechaUltimaActualizacion = "2026-10-10",
                        observacion = "Pendiente coordinar reunión exploratoria con el cliente."
                    )
                )

                oportunidadesPrueba.forEach { oportunidad ->
                    oportunidadDao.insertOportunidad(oportunidad)
                }
            }
        }
    }
}