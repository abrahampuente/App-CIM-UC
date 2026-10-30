package cl.uc.cimuce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.Integrante
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Cafe
import cl.uc.cimuce.ui.theme.ChipVerdeTexto
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.VerdeClaro
import cl.uc.cimuce.ui.theme.VerdeMedio
import cl.uc.cimuce.ui.theme.VerdeOscuro

private val ColoresAvatar = listOf(VerdeMedio, Cafe, VerdeClaro)
private val Titulos = setOf("dr.", "dra.", "arq.", "ing.", "mg.")

/** Iniciales del nombre sin el título académico: "Dra. Valentina Rocamora" → "VR". */
fun inicialesDe(nombreCompleto: String): String =
    nombreCompleto.split(" ")
        .filter { it.isNotBlank() && it.lowercase() !in Titulos }
        .take(2)
        .joinToString("") { it.first().uppercase() }

/** Círculo con iniciales sobre color alternado y borde verdeOscuro de 3dp. */
@Composable
fun AvatarCirculo(
    integrante: Integrante,
    indice: Int,
    modifier: Modifier = Modifier,
    tamano: Dp = 76.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .background(ColoresAvatar[indice % ColoresAvatar.size], CircleShape)
            .border(3.dp, VerdeOscuro, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inicialesDe(integrante.nombreCompleto),
            fontFamily = SerifTitulos,
            fontSize = (tamano.value * 24 / 76).sp, // 24sp en el tamaño base de 76dp
            color = Color.White,
            modifier = Modifier.clearAndSetSemantics {} // TalkBack lee el nombre, no las iniciales
        )
    }
}

/**
 * Avatar circular de 76dp con iniciales y badge →, más el nombre corto debajo.
 * indice alterna el color de fondo (verdeMedio, cafe, verdeClaro).
 */
@Composable
fun AvatarIntegrante(
    integrante: Integrante,
    indice: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClickLabel = "Ver relaciones", onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(80.dp)) {
            AvatarCirculo(integrante, indice, Modifier.align(Alignment.Center))
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.BottomEnd)
                    .background(ChipVerdeTexto, CircleShape)
                    .border(2.dp, Crema, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = integrante.nombreCorto,
            fontFamily = SansCuerpo,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            color = TextoCuerpo,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE)
@Composable
private fun AvatarIntegrantePreview() {
    CIMUCETheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ProyectoRepository.getIntegrantesDe(4).forEachIndexed { i, integrante ->
                AvatarIntegrante(integrante, i, onClick = {})
            }
        }
    }
}
