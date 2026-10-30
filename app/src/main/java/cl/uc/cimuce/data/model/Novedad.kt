package cl.uc.cimuce.data.model

import androidx.annotation.DrawableRes

data class Novedad(
    val idNovedad: Int,
    val titulo: String,
    val resumen: String,
    val fecha: String,
    @param:DrawableRes val imagen: Int
)
