package cl.uc.cimuce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.ui.theme.BordeChip
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.TextoChip
import cl.uc.cimuce.ui.theme.VerdeOscuro

/**
 * Chip de filtro en forma de píldora.
 * Normal (40dp, 14sp) para tipos; pequeño (32dp, 13sp) para áreas.
 */
@Composable
fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pequeno: Boolean = false
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize() // área táctil de 48dp aunque el chip mida 32–40dp
            .height(if (pequeno) 32.dp else 40.dp)
            .clip(CircleShape)
            .background(if (seleccionado) VerdeOscuro else Color.Transparent)
            .border(1.dp, if (seleccionado) VerdeOscuro else BordeChip, CircleShape)
            .selectable(selected = seleccionado, role = Role.Checkbox, onClick = onClick)
            .padding(horizontal = if (pequeno) 14.dp else 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontFamily = SansCuerpo,
            fontSize = if (pequeno) 13.sp else 14.sp,
            fontWeight = if (seleccionado) FontWeight.Medium else FontWeight.Normal,
            color = if (seleccionado) Color.White else TextoChip,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE)
@Composable
private fun ChipFiltroPreview() {
    CIMUCETheme {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro("Todos", seleccionado = true, onClick = {})
                ChipFiltro("Proyectos", seleccionado = false, onClick = {})
            }
            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro("Sustentabilidad", seleccionado = true, onClick = {}, pequeno = true)
                ChipFiltro("Normalización", seleccionado = false, onClick = {}, pequeno = true)
            }
        }
    }
}
