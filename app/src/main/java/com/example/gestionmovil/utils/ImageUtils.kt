package com.example.gestionmovil.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    /**
     * Guarda un Bitmap en el almacenamiento privado de la app y retorna la ruta absoluta.
     */
    fun guardarBitmapEnAlmacenamiento(context: Context, bitmap: Bitmap, nombreArchivo: String): String {
        val archivo = File(context.filesDir, nombreArchivo)
        FileOutputStream(archivo).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return archivo.absolutePath
    }

    /**
     * Carga un Bitmap guardado previamente en el almacenamiento del dispositivo desde su ruta.
     */
    fun cargarBitmapDesdeRuta(rutaArchivo: String?): Bitmap? {
        if (rutaArchivo.isNullOrEmpty()) return null
        val archivo = File(rutaArchivo)
        return if (archivo.exists()) {
            BitmapFactory.decodeFile(archivo.absolutePath)
        } else {
            null
        }
    }
}