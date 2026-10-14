# GestiónMóvil - DSY1105_010D DESARROLLO APP MÓVILES

GestiónMóvil es una aplicación móvil Android orientada al registro, organización, priorización y seguimiento de oportunidades comerciales y activaciones corporativas, centralizando eventos, organizaciones, contactos y gestiones comerciales mediante Kotlin, Jetpack Compose y SQLite.

## Tecnologías

- **Lenguaje:** Kotlin
- **Entorno:** Android Studio
- **Interfaz:** Jetpack Compose y Material Design 3
- **Navegación:** Navigation Compose
- **Arquitectura:** MVVM
- **Persistencia:** SQLite
- **Control de versiones:** Git y GitHub

## Estructura del proyecto

```text
GestionMovil/
├── README.md
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/cl/duoc/gestionmovil/
        │   │   ├── MainActivity.kt
        │   │   ├── GestionMovilApp.kt
        │   │   ├── data/
        │   │   ├── model/
        │   │   ├── navigation/
        │   │   ├── ui/
        │   │   ├── util/
        │   │   └── navigation/
        │   └── res/
        ├── test/
        └── androidTest/
```

## Módulos funcionales

- **Autenticación:** inicio de sesión con cuentas ficticias.
- **Oportunidades:** registro, consulta, edición, priorización y actualización de estados.
- **Eventos:** administración de fechas, horarios, ubicaciones y condiciones de participación.
- **Organizaciones y contactos:** registro y asociación de organizaciones con contactos corporativos ficticios.
- **Seguimiento:** registro de gestiones, propuestas, respuestas y próximas acciones.
- **Alertas e historial:** consulta de eventos próximos, gestiones pendientes y acciones realizadas.