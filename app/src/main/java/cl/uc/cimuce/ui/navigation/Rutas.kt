package cl.uc.cimuce.ui.navigation

/** Rutas de la app. Profundidad máxima: Inicio > Explorar > Detalle (NF2). */
object Rutas {
    const val ONBOARDING = "onboarding"
    const val PRINCIPAL = "principal" // grafo anidado con todo lo que muestra la barra inferior

    const val INICIO = "inicio"
    const val NOVEDADES = "novedades"
    const val CONTACTO = "contacto"

    const val ARG_TIPO = "tipo"
    const val ARG_Q = "q"
    const val EXPLORAR = "explorar?$ARG_TIPO={$ARG_TIPO}&$ARG_Q={$ARG_Q}"

    /** Clave en el SavedStateHandle de la entrada: se abrió desde contenido (no desde la pestaña). */
    const val CON_VOLVER = "conVolver"

    const val ARG_ID_PROYECTO = "idProyecto"
    const val DETALLE = "detalle/{$ARG_ID_PROYECTO}"

    /** tipo es el name() de TipoContenido. */
    fun explorar(tipo: String? = null, q: String? = null): String {
        val params = buildList {
            tipo?.let { add("$ARG_TIPO=$it") }
            q?.takeIf { it.isNotBlank() }?.let { add("$ARG_Q=${android.net.Uri.encode(it)}") }
        }
        return if (params.isEmpty()) "explorar" else "explorar?" + params.joinToString("&")
    }

    fun detalle(idProyecto: Int) = "detalle/$idProyecto"
}
