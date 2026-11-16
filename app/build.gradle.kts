plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.example.gestionmovil"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.gestionmovil"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

// Configuración de lo que debe cubrir Kover en los tests.
kover {
    reports {
        filters {
            includes {
                classes(
                     // Utilidades y lógica pura
                    "com.example.gestionmovil.utils.Validaciones*",
                    "com.example.gestionmovil.utils.ValidacionesRegistro*",
                    "com.example.gestionmovil.utils.Seguridad*",

                    // Mappers de Red (Conversión Retrofit DTOs <-> Entities)
                    "com.example.gestionmovil.data.remote.mapper.OportunidadMapper*",
                    "com.example.gestionmovil.data.remote.mapper.UsuarioMapper*",

                    // Repositorios (Lógica de negocio y datos)
                    "com.example.gestionmovil.data.repository.UsuarioRepository*",
                    "com.example.gestionmovil.data.repository.OportunidadRepository*",
                    "com.example.gestionmovil.data.repository.OrganizacionRepository*",
                    "com.example.gestionmovil.data.repository.ContactoRepository*",
                    "com.example.gestionmovil.data.repository.EventoRepository*",
                    "com.example.gestionmovil.data.repository.SeguimientoRepository*",

                    // ViewModels (Lógica de presentación y estados)
                    /*
                    "com.example.gestionmovil.ui.screens.login.LoginViewModel*",
                    "com.example.gestionmovil.ui.theme.Registro.RegistroViewModel*",
                    "com.example.gestionmovil.ui.screens.InicioViewModel*",
                    "com.example.gestionmovil.ui.seguimiento.SeguimientoViewModel*",
                    "com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModel*",
                    "com.example.gestionmovil.ui.screens.evento_form.EventoFormViewModel*",
                    "com.example.gestionmovil.ui.screens.oportunidad_form.OportunidadFormViewModel*",
                    "com.example.gestionmovil.ui.screens.oportunidad_list.OportunidadListViewModel*",
                    "com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModel*"*/
                )
            }
        }
    }
}

dependencies {
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.room.common)
    val room_version = "3.0.3"
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-core")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Para resolver 'viewModel()'
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    // Para resolver los íconos de visibilidad (Visibility y VisibilityOff)
    // implementation(libs.androidx.room3.runtime)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
}