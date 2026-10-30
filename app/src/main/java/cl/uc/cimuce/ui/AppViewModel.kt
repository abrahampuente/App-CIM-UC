package cl.uc.cimuce.ui

import androidx.lifecycle.ViewModel
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Estado global de la sesión, compartido a nivel de Activity. */
class AppViewModel : ViewModel() {

    private val _perfil = MutableStateFlow<PerfilUsuario?>(null)
    val perfil: StateFlow<PerfilUsuario?> = _perfil.asStateFlow()

    private val _favoritos = MutableStateFlow<Set<Int>>(emptySet())
    val favoritos: StateFlow<Set<Int>> = _favoritos.asStateFlow()

    fun elegirPerfil(perfil: PerfilUsuario) {
        _perfil.value = perfil
    }

    fun toggleFavorito(id: Int) {
        _favoritos.update { actuales -> if (id in actuales) actuales - id else actuales + id }
    }

    /**
     * Modo Feria: simula el escaneo del QR de un stand guardando un ítem aleatorio
     * que aún no esté en favoritos. Devuelve null si ya están todos guardados.
     */
    fun guardarDesdeFeria(): Proyecto? {
        val elegido = ProyectoRepository.getTodos()
            .filter { it.idProyecto !in _favoritos.value }
            .randomOrNull()
            ?: return null
        _favoritos.update { it + elegido.idProyecto }
        return elegido
    }
}
