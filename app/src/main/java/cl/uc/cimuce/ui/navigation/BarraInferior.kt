package cl.uc.cimuce.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.Etiqueta
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.VerdeOscuro

/** Barra inferior personalizada: fondo crema, borde superior y punto bajo la pestaña activa. */
@Composable
fun BarraInferior(
    pestanaActiva: Pestana?,
    onPestana: (Pestana) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Crema)
            .drawBehind {
                val grosor = 1.dp.toPx()
                drawLine(BordeSuave, Offset(0f, grosor / 2), Offset(size.width, grosor / 2), grosor)
            }
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Pestana.entries.forEach { pestana ->
            ItemBarra(
                pestana = pestana,
                activa = pestana == pestanaActiva,
                onClick = { onPestana(pestana) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ItemBarra(
    pestana: Pestana,
    activa: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.selectable(
            selected = activa,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = false),
            role = Role.Tab,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (activa) pestana.iconoActivo else pestana.iconoInactivo,
            contentDescription = null,
            tint = if (activa) VerdeOscuro else Etiqueta
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = pestana.etiqueta,
            fontFamily = SansCuerpo,
            fontSize = 13.sp,
            fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal,
            color = if (activa) TextoCuerpo else Etiqueta
        )
        Spacer(Modifier.height(3.dp))
        Box(
            Modifier
                .size(4.dp)
                .background(if (activa) VerdeOscuro else Color.Transparent, CircleShape)
        )
    }
}
