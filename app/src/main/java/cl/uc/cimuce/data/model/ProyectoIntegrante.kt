package cl.uc.cimuce.data.model

/** Tabla intermedia de la relación muchos a muchos Proyecto ↔ Integrante. */
data class ProyectoIntegrante(
    val idRelacion: Int,
    val idProyecto: Int,
    val idIntegrante: Int
)
