package com.example.gestionmovil.ui.screens.oportunidad_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionmovil.data.repository.OportunidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OportunidadListViewModel(
    private val oportunidadRepository: OportunidadRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _estadoFiltro = MutableStateFlow<String?>(null)
    private val _isFilterVisible = MutableStateFlow(false)

    val uiState: StateFlow<OportunidadListUiState> = combine(
        oportunidadRepository.getOportunidadesConOrganizacion(),
        _searchQuery,
        _estadoFiltro,
        _isFilterVisible
    ) { lista, query, estado, isFilterVisible ->

        val filtradas = lista.filter { item ->
            val nombreOrg = item.organizacion?.nombre ?: ""
            val coincideBusqueda = item.oportunidad.nombre.contains(query, ignoreCase = true) ||
                    nombreOrg.contains(query, ignoreCase = true)

            val coincideEstado = estado == null || item.oportunidad.estado.equals(estado, ignoreCase = true)

            coincideBusqueda && coincideEstado
        }

        OportunidadListUiState(
            searchQuery = query,
            estadoFiltro = estado,
            showFilterSheet = isFilterVisible,
            oportunidades = filtradas,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OportunidadListUiState(isLoading = true)
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onEstadoFiltroSelected(nuevoEstado: String?) {
        _estadoFiltro.value = nuevoEstado
    }

    fun toggleFilterVisibility() {
        _isFilterVisible.value = !_isFilterVisible.value
    }
}