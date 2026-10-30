package cl.uc.cimuce.ui.screens.inicio

import androidx.lifecycle.ViewModel
import cl.uc.cimuce.data.model.Novedad
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.data.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class InicioUiState(
    val perfil: PerfilUsuario? = null,
    val recomendados: List<Proyecto> = emptyList(),
    val conteos: Map<TipoContenido, Int> = emptyMap(),
    val novedadDestacada: Novedad? = null,
    val recientes: List<Proyecto> = emptyList()
)

class InicioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        InicioUiState(
            conteos = TipoContenido.entries.associateWith { ProyectoRepository.contarPorTipo(it) },
            novedadDestacada = ProyectoRepository.getNovedades().firstOrNull(),
            recientes = ProyectoRepository.getRecientes(3)
        )
    )
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    /** El perfil vive en AppViewModel; aquí solo se recalculan sus recomendados. */
    fun actualizarPerfil(perfil: PerfilUsuario?) {
        if (perfil == _uiState.value.perfil) return
        _uiState.update {
            it.copy(
                perfil = perfil,
                recomendados = perfil?.let(ProyectoRepository::getRecomendados).orEmpty()
            )
        }
    }
}
