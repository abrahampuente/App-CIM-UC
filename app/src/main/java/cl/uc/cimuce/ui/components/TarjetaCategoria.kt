package cl.uc.cimuce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Cafe
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.VerdeMedio
import cl.uc.cimuce.ui.theme.VerdeOscuro

/**
 * Tarjeta de categoría con fondo de color, ícono, título y subtítulo en blanco.
 * Vertical (centrada, 150dp de alto) o horizontal (a todo el ancho, 96dp de alto).
 */
@Composable
fun TarjetaCategoria(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    fondo: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false
) {
    val base = modifier
        .clip(RoundedCornerShape(18.dp))
        .background(fondo)
        .clickable(onClick = onClick)

    if (horizontal) {
        Row(
            modifier = base.fillMaxWidth().height(96.dp).padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(18.dp))
            Column { Textos(titulo, subtitulo, TextAlign.Start) }
        }
    } else {
        Column(
            modifier = base.height(150.dp).padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(34.dp))
            Spacer(Modifier.height(10.dp))
            Textos(titulo, subtitulo, TextAlign.Center)
        }
    }
}

@Composable
private fun Textos(titulo: String, subtitulo: String, alineacion: TextAlign) {
    Text(
        text = titulo,
        fontFamily = SerifTitulos,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White,
        textAlign = alineacion
    )
    Spacer(Modifier.height(2.dp))
    Text(
        text = subtitulo,
        fontFamily = SansCuerpo,
        fontSize = 13.sp,
        color = Color.White.copy(alpha = 0.95f),
        textAlign = alineacion
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE, widthDp = 360)
@Composable
private fun TarjetaCategoriaPreview() {
    CIMUCETheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaCategoria("Proyectos", "12 proyectos activos", Icons.Filled.Architecture, VerdeOscuro, {}, Modifier.weight(1f))
                TarjetaCategoria("Capacidades I+D", "8 líneas de trabajo", Icons.Filled.Biotech, Cafe, {}, Modifier.weight(1f))
            }
            TarjetaCategoria("Publicaciones", "6 documentos", Icons.Filled.Description, VerdeMedio, {}, horizontal = true)
        }
    }
}
