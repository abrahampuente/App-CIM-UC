package cl.uc.cimuce.data.model

import androidx.annotation.DrawableRes

/**
 * Ítem de contenido (proyecto, capacidad I+D o publicación).
 * urlImagen se conserva por fidelidad al modelo de datos documentado; la UI usa imagenRes.
 */
data class Proyecto(
    val idProyecto: Int,
    val titulo: String,
    val descripcionBreve: String,
    val descripcionDetallada: String,
    val tipoContenido: TipoContenido,
    val areaTematica: String,
    val anoEjecucion: Int,
    val estadoProyecto: String,
    val urlImagen: String,
    @param:DrawableRes val imagenRes: Int,
    val perfilesAfines: List<PerfilUsuario>
)
