package com.example.gestionmovil.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CamaraUtils {
    fun creaUriFotografia(context: Context): Uri {
        // 1. Asegurar la creación de la carpeta "fotos" en cacheDir
        val carpeta = File(context.cacheDir, "fotos")
        if (!carpeta.exists()) {
            carpeta.mkdirs()
        }

        // 2. Generar el archivo temporal
        val archivo = File.createTempFile("foto_perfil_", ".jpg", carpeta)

        // 3. Forzar la creación física del archivo en disco si no existe
        if (!archivo.exists()) {
            archivo.createNewFile()
        }

        // 4. Retornar la URI vía FileProvider
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }
}