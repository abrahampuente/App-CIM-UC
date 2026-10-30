package cl.uc.cimuce.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario

/** Título serif de 26sp y subtítulo, para las pestañas Novedades y Contacto. */
@Composable
fun EncabezadoPestana(titulo: String, subtitulo: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = titulo,
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = TextoCuerpo,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = subtitulo,
            fontFamily = SansCuerpo,
            fontSize = 15.sp,
            color = TextoSecundario,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE)
@Composable
private fun EncabezadoPestanaPreview() {
    CIMUCETheme { EncabezadoPestana("Novedades", "Actividad reciente del CIM UC", Modifier.padding(20.dp)) }
}
