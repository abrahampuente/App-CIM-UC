package cl.uc.cimuce.ui.screens.explorar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.navigation.Rutas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Filtros de Explorar (Req F1). tipo y q llegan como argumentos de navegación en el
 * SavedStateHandle; los cambios se guardan de vuelta ahí para sobrevivir a la recreación.
 */
class ExplorarViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _tipo = MutableStateFlow(
        savedStateHandle.get<String>(Rutas.ARG_TIPO)
            ?.let { nombre -> TipoContenido.entries.find { it.name == nombre } }
    )
    val tipo: StateFlow<TipoContenido?> = _tipo.asStateFlow()

    private val _area = MutableStateFlow(savedStateHandle.get<String>(CLAVE_AREA))
    val area: StateFlow<String?> = _area.asStateFlow()

    private val _texto = MutableStateFlow(savedStateHandle.get<String>(Rutas.ARG_Q).orEmpty())
    val texto: StateFlow<String> = _texto.asStateFlow()

    val resultados: StateFlow<List<Proyecto>> =
        combine(_tipo, _area, _texto) { tipo, area, texto ->
            ProyectoRepository.filtrar(tipo, area, texto)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProyectoRepository.filtrar(_tipo.value, _area.value, _texto.value)
        )

    fun elegirTipo(tipo: TipoContenido?) {
        _tipo.value = tipo
        savedStateHandle[Rutas.ARG_TIPO] = tipo?.name
    }

    /** Las áreas son opcionales y excluyentes: tocar la activa la desactiva. */
    fun alternarArea(area: String) {
        val nueva = if (_area.value == area) null else area
        _area.value = nueva
        savedStateHandle[CLAVE_AREA] = nueva
    }

    fun cambiarTexto(texto: String) {
        _texto.value = texto
        savedStateHandle[Rutas.ARG_Q] = texto
    }

    fun limpiarFiltros() {
        elegirTipo(null)
        _area.value = null
        savedStateHandle[CLAVE_AREA] = null
        cambiarTexto("")
    }

    private companion object {
        const val CLAVE_AREA = "area"
    }
}
