package com.example.gestionmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.gestionmovil.data.local.AppDatabase
import com.example.gestionmovil.data.local.entities.OportunidadEntity
import com.example.gestionmovil.data.repository.OportunidadRepository
import com.example.gestionmovil.data.repository.OrganizacionRepository
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormScreen
import com.example.gestionmovil.ui.screens.contacto_form.ContactoFormViewModel
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailScreen
import com.example.gestionmovil.ui.screens.oportunidad_detail.OportunidadDetailViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val oportunidadRepository = OportunidadRepository(database.oportunidadDao())

        // 2. Instanciar ViewModel
        val oportunidadDetailViewModel = OportunidadDetailViewModel(oportunidadRepository)

        setContent {
            MaterialTheme {
                Surface {
                    // 3. Probar la pantalla de formulario
                    OportunidadDetailScreen(
                        oportunidadId = 1,
                        viewModel = oportunidadDetailViewModel,
                        onNavigateBack = { /* Accion al volver */ }
                    )
                }
            }
        }
    }
}