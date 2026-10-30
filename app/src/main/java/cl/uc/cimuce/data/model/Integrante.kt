package cl.uc.cimuce.data.model

data class Integrante(
    val idIntegrante: Int,
    val nombreCompleto: String,
    val nombreCorto: String,
    val rolCargo: String,
    val organizacionAsociada: String
)
