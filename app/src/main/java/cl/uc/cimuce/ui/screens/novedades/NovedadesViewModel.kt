package cl.uc.cimuce.ui.screens.novedades

import androidx.lifecycle.ViewModel
import cl.uc.cimuce.data.model.Novedad
import cl.uc.cimuce.data.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NovedadesViewModel : ViewModel() {

    private val _novedades = MutableStateFlow(ProyectoRepository.getNovedades())
    val novedades: StateFlow<List<Novedad>> = _novedades.asStateFlow()

    private val _expandidas = MutableStateFlow<Set<Int>>(emptySet())
    val expandidas: StateFlow<Set<Int>> = _expandidas.asStateFlow()

    fun alternar(idNovedad: Int) {
        _expandidas.update { if (idNovedad in it) it - idNovedad else it + idNovedad }
    }
}
