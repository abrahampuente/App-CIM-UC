package cl.uc.cimuce.data.model

object AreaTematica {
    const val INGENIERIA_ESTRUCTURAL = "Ingeniería Estructural"
    const val CONSTRUCCION_INDUSTRIALIZADA = "Construcción Industrializada"
    const val SUSTENTABILIDAD = "Sustentabilidad"
    const val NORMALIZACION = "Normalización"

    val todas = listOf(INGENIERIA_ESTRUCTURAL, CONSTRUCCION_INDUSTRIALIZADA, SUSTENTABILIDAD, NORMALIZACION)
}

object EstadoProyecto {
    const val EN_CURSO = "En curso"
    const val FINALIZADO = "Finalizado"
    const val PUBLICADO = "Publicado"
}
