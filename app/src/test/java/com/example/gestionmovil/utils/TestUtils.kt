package com.example.gestionmovil.utils

import android.app.Application
import android.content.Context
import android.util.Patterns
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.fakes.FakeUsuarioDao
import java.lang.reflect.Field
import java.util.regex.Pattern

object TestUtils {

    fun initPatterns() {
        try {
            val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
            val field = Patterns::class.java.getDeclaredField("EMAIL_ADDRESS")

            val unsafeClass = Class.forName("sun.misc.Unsafe")
            val theUnsafeField = unsafeClass.getDeclaredField("theUnsafe")
            theUnsafeField.isAccessible = true
            val unsafe = theUnsafeField.get(null)

            val staticFieldBase = unsafeClass.getMethod("staticFieldBase", Field::class.java).invoke(unsafe, field)
            val staticFieldOffset = unsafeClass.getMethod("staticFieldOffset", Field::class.java).invoke(unsafe, field) as Long

            unsafeClass.getMethod("putObject", Any::class.java, Long::class.javaPrimitiveType, Any::class.java)
                .invoke(unsafe, staticFieldBase, staticFieldOffset, emailPattern)
        } catch (_: Exception) {
        }
    }

    fun injectFakeDatabase(fakeUsuarioDao: FakeUsuarioDao) {
        val fakeDb = object : AppDatabase() {
            override fun usuarioDao() = fakeUsuarioDao
            override fun seguimientoDao() = TODO()
            override fun oportunidadDao() = TODO()
            override fun organizacionDao() = TODO()
            override fun eventoDao() = TODO()
            override fun contactoDao() = TODO()
            override fun createInvalidationTracker() = TODO()
            override suspend fun clearAllTables() = TODO()
        }

        try {
            val field: Field = AppDatabase.Companion::class.java.getDeclaredField("INSTANCE")
            field.isAccessible = true
            field.set(AppDatabase.Companion, fakeDb)
        } catch (_: Exception) {
        }
        try {
            val field: Field = AppDatabase::class.java.getDeclaredField("INSTANCE")
            field.isAccessible = true
            field.set(null, fakeDb)
        } catch (_: Exception) {
        }
    }

    fun createMockApplication(): Application {
        return object : Application() {
            override fun getApplicationContext(): Context {
                return this
            }
        }
    }
}
