package com.example.gestionmovil.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Entidades de color separadas para permitir la modificación manual
 * e individual de los componentes (botones, fondo, barras, tarjetas, textos)
 * sin depender exclusivamente de los valores por defecto de Material.
 */
data class AppColors(
    val background: Color,          // [FONDO] Color general del fondo de las pantallas (Scaffold / Surface principal)
    val surface: Color,             // [SUPERFICIE] Color base para contenedores, hojas modales y diálogos
    val primaryButton: Color,       // [BOTÓN PRINCIPAL] Color de fondo de los botones de acción principal (Ej: Iniciar Sesión, Guardar)
    val onPrimaryButton: Color,     // [TEXTO BOTÓN PRINCIPAL] Color del texto e iconos sobre los botones principales
    val secondaryButton: Color,     // [BOTÓN SECUNDARIO] Color de fondo para botones secundarios o de contorno
    val onSecondaryButton: Color,   // [TEXTO BOTÓN SECUNDARIO] Color del texto sobre los botones secundarios
    val topBarBackground: Color,    // [BARRA SUPERIOR] Color de fondo de la barra de navegación superior (TopAppBar)
    val topBarContent: Color,       // [CONTENIDO BARRA SUPERIOR] Color de los títulos, íconos y botones en la TopAppBar
    val bottomBarBackground: Color, // [BARRA INFERIOR] Color de fondo de la barra de navegación inferior (BottomBar / NavigationBar)
    val bottomBarContent: Color,    // [CONTENIDO BARRA INFERIOR] Color de los íconos y etiquetas en la barra inferior
    val cardBackground: Color,      // [TARJETAS / CARDS] Color de fondo específico para tarjetas y elementos agrupados en listas
    val textPrimary: Color,         // [TEXTO PRINCIPAL] Color para títulos, encabezados y texto de alta visibilidad
    val textSecondary: Color,       // [TEXTO SECUNDARIO] Color para subtítulos, etiquetas y texto de menor jerarquía
    val isDark: Boolean             // [ESTADO TEMA] Indica si el esquema activo corresponde al modo oscuro
)

val LightAppColors = AppColors(
    background = EmeraldBackground,
    surface = EmeraldSurface,
    primaryButton = EmeraldPrimary,
    onPrimaryButton = Color.White,
    secondaryButton = Color(0xFFE2E8F0),
    onSecondaryButton = EmeraldTextPrimary,
    topBarBackground = EmeraldPrimary,
    topBarContent = Color.White,
    bottomBarBackground = EmeraldSurface,
    bottomBarContent = EmeraldPrimary,
    cardBackground = EmeraldCardBg,
    textPrimary = EmeraldTextPrimary,
    textSecondary = EmeraldTextSecondary,
    isDark = false
)

val DarkAppColors = AppColors(
    background = DarkBackground,
    surface = DarkSurface,
    primaryButton = DarkPrimary,
    onPrimaryButton = Color(0xFF0F172A),
    secondaryButton = Color(0xFF1E293B),
    onSecondaryButton = DarkTextPrimary,
    topBarBackground = DarkSurface,
    topBarContent = DarkTextPrimary,
    bottomBarBackground = DarkSurface,
    bottomBarContent = DarkPrimary,
    cardBackground = DarkSurface,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = EmeraldSecondary,
    tertiary = EmeraldPrimaryLight,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color(0xFF0F172A),
    onSecondary = Color.White,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    secondary = EmeraldSecondary,
    tertiary = EmeraldPrimaryDark,
    background = EmeraldBackground,
    surface = EmeraldSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = EmeraldTextPrimary,
    onSurface = EmeraldTextPrimary
)

@Composable
fun GestionMovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivamos dynamicColor por defecto para garantizar que se aplique siempre
    // el esquema profesional de verde esmeralda y blanco solicitado.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val appColors = if (darkTheme) DarkAppColors else LightAppColors
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
