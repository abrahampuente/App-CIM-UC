package cl.uc.cimuce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.EstadoProyecto
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.ChipCafeFondo
import cl.uc.cimuce.ui.theme.ChipCafeTexto
import cl.uc.cimuce.ui.theme.ChipVerdeFondo
import cl.uc.cimuce.ui.theme.ChipVerdeTexto
import cl.uc.cimuce.ui.theme.EstadoFinalizadoFondo
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.TextoChip

/** Colores (fondo, texto) por estado. */
private fun coloresDe(estado: String): Pair<Color, Color> = when (estado) {
    EstadoProyecto.EN_CURSO -> ChipVerdeFondo to ChipVerdeTexto
    EstadoProyecto.PUBLICADO -> ChipCafeFondo to ChipCafeTexto
    else -> EstadoFinalizadoFondo to TextoChip
}

/** Chip de estado: En curso (verde), Finalizado (gris cálido) o Publicado (café). */
@Composable
fun ChipEstado(estado: String, modifier: Modifier = Modifier) {
    val (fondo, texto) = coloresDe(estado)
    Text(
        text = estado,
        color = texto,
        fontFamily = SansCuerpo,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        modifier = modifier
            .background(fondo, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ChipEstadoPreview() {
    CIMUCETheme {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChipEstado(EstadoProyecto.EN_CURSO)
            ChipEstado(EstadoProyecto.FINALIZADO)
            ChipEstado(EstadoProyecto.PUBLICADO)
        }
    }
}
