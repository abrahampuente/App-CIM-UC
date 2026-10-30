package cl.uc.cimuce.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.EstiloEtiquetaSeccion

/** Etiqueta de sección en MAYÚSCULAS (12sp, SemiBold, espaciado 1.5sp, color etiqueta). */
@Composable
fun EtiquetaSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto.uppercase(),
        style = EstiloEtiquetaSeccion,
        modifier = modifier.semantics { heading() } // TalkBack permite saltar entre secciones
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE)
@Composable
private fun EtiquetaSeccionPreview() {
    CIMUCETheme { EtiquetaSeccion("Proyectos recientes") }
}
