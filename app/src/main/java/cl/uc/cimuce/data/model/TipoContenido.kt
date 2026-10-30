package cl.uc.cimuce.data.model

/** Tipo de contenido; define los chips de filtro principales. */
enum class TipoContenido(val etiqueta: String) {
    PROYECTO("Proyectos"),
    CAPACIDAD_ID("Capacidades I+D"),
    PUBLICACION("Publicaciones")
}
