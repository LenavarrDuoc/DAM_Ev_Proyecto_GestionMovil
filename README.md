# GestiónMóvil - DSY1105_010D Desarrollo de Aplicaciones Móviles

**GestiónMóvil** es una aplicación móvil Android nativa diseñada para la gestión operativa y comercial de oportunidades, organizaciones, contactos, seguimientos y eventos corporativos. Ofrece control de acceso basado en roles (RBAC) e integra características nativas del dispositivo como la cámara y el calendario.

---

## 👥 Integrantes

- **[Benjamín Albornoz]** - *[be.albornozc@duocuc.cl]*
- **[Leonardo Navarro]** - *[leo.navarro@duocuc.cl]*
- **[Hans Pizarro]** - *[ha.araya@duocuc.cl]*

---

## 🛠️ Tecnologías y Herramientas

- **Lenguaje:** Kotlin
- **UI Framework:** Jetpack Compose + Material Design 3
- **Navegación:** Navigation Compose (Single-Activity Architecture)
- **Patrón de Arquitectura:** MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF)
- **Persistencia Local:** Room 3 (SQLite) + Flow (StateFlow)
- **Integración de Hardware Nativo:** 
  - Cámara Nativa (`ActivityResultContracts.TakePicturePreview`)
  - Calendario Nativo (`Intent.ACTION_INSERT` con `CalendarContract`)
- **Control de Versiones:** Git & GitHub

---

## 📱 Módulos Funcionales

1. **Autenticación y Roles (RBAC):**
   - Control de acceso para **Ejecutivo Comercial** y **Supervisor Comercial**.
   - Cuentas de prueba pre-cargadas (3 Ejecutivos, 1 Supervisor y 1 Administrador).
   - Vistas y acciones dinámicas según el rol del usuario autenticado (asignación/reasignación de oportunidades).

2. **Oportunidades Comerciales:**
   - Registro, listado, filtrado, búsqueda y detalle de oportunidades.
   - Cambio de estados operacionales y vinculación con organizaciones y contactos.

3. **Eventos y Activaciones Corporativas:**
   - Flujo de 3 pantallas: **Listado / Calendario**, **Creación / Formulario** y **Detalle del Evento**.
   - Captura obligatoria de foto de evidencia con la **Cámara Nativa** y guardado persistente en disco.
   - Agendamiento directo e interacción con el **Calendario Nativo** del dispositivo.

4. **Organizaciones y Contactos:**
   - Gestión de empresas y sus contactos clave asociados bajo normas de normalización (3FN).

5. **Seguimientos y Bitácora:**
   - Historial de interacciones y avances vinculados en relación 1:N con cada oportunidad comercial.

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue la arquitectura **MVVM + UDF**, manteniendo una única fuente de verdad (*Single Source of Truth*) mediante **StateFlow** y **Room**:

```text
GestionMovil/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/gestionmovil/
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── AppDatabase.kt          # Base de datos Room3
│           │   │   │   ├── dao/                    # DAOs (Usuario, Evento, Oportunidad, etc.)
│           │   │   │   └── entities/               # Entidades Room
│           │   │   └── repository/                 # Repositorios (Capa de datos unificada)
│           │   ├── ui/
│           │   │   ├── navigation/
│           │   │   │   ├── AppNavegacion.kt        # Graph de navegación central (NavHost)
│           │   │   │   └── Rutas.kt                # Definición de rutas sealed class
│           │   │   ├── screens/                    # Pantallas divididas por módulo
│           │   │   │   ├── auth/
│           │   │   │   ├── eventos/                # EventoList, EventoForm, EventoDetail
│           │   │   │   ├── oportunidades/
│           │   │   │   └── home/
│           │   │   └── theme/                      # Paleta de colores y estilos MD3
│           │   └── utils/
│           │       ├── ImageUtils.kt               # Utilidades de almacenamiento de imágenes
│           │       └── Seguridad.kt                # Hasheado de contraseñas
│           └── AndroidManifest.xml
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Instrucciones de Ejecución

### Prerrequisitos

- **Android Studio:** Hedgehog (2023.1.1) o superior.
- **JDK:** Java 17 o superior.
- **Dispositivo / Emulador:** Android 8.0 (API Level 26) o superior con servicios de Cámara y Calendario habilitados.

### Pasos para compilar y ejecutar

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/gestion-movil.git
   cd gestion-movil
   ```

2. **Abrir en Android Studio:**
   - Selecciona `File > Open` y busca el directorio raíz del proyecto.
   - Espera a que Gradle sincronice las dependencias (`Sync Project with Gradle Files`).

3. **Ejecutar en Emulador o Dispositivo:**
   - Selecciona un emulador (AVD) o conecta un dispositivo físico mediante USB Debugging.
   - Presiona `Shift + F10` o el botón **Run ('app')**.

---

## 🔑 Cuentas de Prueba (Pre-cargadas)

| Rol | Correo Electrónico | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `admin@empresa.cl` | `Clave123+` |
| **Supervisor** | `supervisor@empresa.cl` | `Clave123+` |
| **Ejecutivo 1** | `ejecutivo1@empresa.cl` | `Clave123+` |
| **Ejecutivo 2** | `ejecutivo2@empresa.cl` | `Clave123+` |
| **Ejecutivo 3** | `ejecutivo3@empresa.cl` | `Clave123+` |