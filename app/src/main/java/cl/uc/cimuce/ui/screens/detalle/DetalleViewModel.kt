package cl.uc.cimuce.ui.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cl.uc.cimuce.data.model.Integrante
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.navigation.Rutas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Detalle de un ítem (Req F2): el id llega como argumento de navegación. */
class DetalleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val idProyecto: Int = savedStateHandle.get<Int>(Rutas.ARG_ID_PROYECTO) ?: -1

    private val _proyecto = MutableStateFlow(ProyectoRepository.getPorId(idProyecto))
    val proyecto: StateFlow<Proyecto?> = _proyecto.asStateFlow()

    private val _integrantes = MutableStateFlow(ProyectoRepository.getIntegrantesDe(idProyecto))
    val integrantes: StateFlow<List<Integrante>> = _integrantes.asStateFlow()

    /** Iniciativas de un integrante, para el bottom sheet de relaciones (Req F3). */
    fun iniciativasDe(idIntegrante: Int): List<Proyecto> =
        ProyectoRepository.getProyectosDeIntegrante(idIntegrante)
}
