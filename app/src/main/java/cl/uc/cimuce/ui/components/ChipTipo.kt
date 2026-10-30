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
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.ChipCafeFondo
import cl.uc.cimuce.ui.theme.ChipCafeTexto
import cl.uc.cimuce.ui.theme.ChipVerdeFondo
import cl.uc.cimuce.ui.theme.ChipVerdeTexto
import cl.uc.cimuce.ui.theme.SansCuerpo

/** Colores (fondo, texto) por tipo: verde para proyectos y publicaciones, café para capacidades. */
private fun coloresDe(tipo: TipoContenido): Pair<Color, Color> = when (tipo) {
    TipoContenido.CAPACIDAD_ID -> ChipCafeFondo to ChipCafeTexto
    TipoContenido.PROYECTO, TipoContenido.PUBLICACION -> ChipVerdeFondo to ChipVerdeTexto
}

/** Chip pequeño con el tipo de contenido (11sp, esquinas de 6dp). */
@Composable
fun ChipTipo(tipo: TipoContenido, modifier: Modifier = Modifier) {
    val (fondo, texto) = coloresDe(tipo)
    Text(
        text = tipo.etiqueta,
        color = texto,
        fontFamily = SansCuerpo,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        modifier = modifier
            .background(fondo, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ChipTipoPreview() {
    CIMUCETheme {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TipoContenido.entries.forEach { ChipTipo(it) }
        }
    }
}
