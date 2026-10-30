package cl.uc.cimuce.ui.screens.contacto

import androidx.lifecycle.ViewModel
import cl.uc.cimuce.data.model.PerfilUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FormularioContacto(
    val nombre: String = "",
    val organizacion: String = "",
    val perfil: PerfilUsuario? = null,
    val mensaje: String = "",
    /** Se activa al intentar enviar: desde ahí se marcan los campos vacíos. */
    val mostrarErrores: Boolean = false,
    val enviado: Boolean = false
) {
    val nombreInvalido get() = nombre.isBlank()
    val organizacionInvalida get() = organizacion.isBlank()
    val perfilInvalido get() = perfil == null
    val mensajeInvalido get() = mensaje.isBlank()
    val esValido get() = !(nombreInvalido || organizacionInvalida || perfilInvalido || mensajeInvalido)
}

/** Formulario simulado: valida campos vacíos y no envía nada real (Req NF3). */
class ContactoViewModel : ViewModel() {

    private val _formulario = MutableStateFlow(FormularioContacto())
    val formulario: StateFlow<FormularioContacto> = _formulario.asStateFlow()

    private var perfilOnboarding: PerfilUsuario? = null

    /** Precarga el perfil elegido en el onboarding si el usuario aún no eligió otro. */
    fun precargarPerfil(perfil: PerfilUsuario?) {
        perfilOnboarding = perfil
        _formulario.update { if (it.perfil == null) it.copy(perfil = perfil) else it }
    }

    fun cambiarNombre(valor: String) = _formulario.update { it.copy(nombre = valor) }
    fun cambiarOrganizacion(valor: String) = _formulario.update { it.copy(organizacion = valor) }
    fun cambiarPerfil(valor: PerfilUsuario) = _formulario.update { it.copy(perfil = valor) }
    fun cambiarMensaje(valor: String) = _formulario.update { it.copy(mensaje = valor) }

    fun enviar() {
        val actual = _formulario.value
        _formulario.value = if (actual.esValido) {
            // Éxito simulado: limpia el formulario y vuelve a precargar el perfil.
            FormularioContacto(perfil = perfilOnboarding, enviado = true)
        } else {
            actual.copy(mostrarErrores = true)
        }
    }

    fun cerrarExito() = _formulario.update { it.copy(enviado = false) }
}
