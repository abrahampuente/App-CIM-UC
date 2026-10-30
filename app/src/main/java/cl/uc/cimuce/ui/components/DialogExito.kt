package cl.uc.cimuce.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.ChipVerdeTexto
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoSecundario
import cl.uc.cimuce.ui.theme.VerdeOscuro
import cl.uc.cimuce.ui.theme.VerdeTexto

/** Diálogo de éxito simulado (Req F4), reutilizable con cualquier título y mensaje. */
@Composable
fun DialogExito(
    titulo: String,
    mensaje: String,
    onCerrar: () -> Unit,
    textoBoton: String = "Entendido"
) {
    Dialog(onDismissRequest = onCerrar) {
        ContenidoDialogExito(titulo, mensaje, onCerrar, textoBoton)
    }
}

@Composable
private fun ContenidoDialogExito(
    titulo: String,
    mensaje: String,
    onCerrar: () -> Unit,
    textoBoton: String
) {
    // En @Preview se muestra el ícono ya escalado; en la app aparece con un rebote.
    val enPreview = LocalInspectionMode.current
    val escala = remember { Animatable(if (enPreview) 1f else 0f) }
    LaunchedEffect(Unit) {
        escala.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .graphicsLayer {
                        scaleX = escala.value
                        scaleY = escala.value
                    }
                    .background(ChipVerdeTexto, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = titulo,
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                color = VerdeTexto,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = mensaje,
                fontFamily = SansCuerpo,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onCerrar,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro, contentColor = Color.White)
            ) {
                Text(textoBoton, fontFamily = SansCuerpo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF8A7A68, widthDp = 360)
@Composable
private fun DialogExitoPreview() {
    CIMUCETheme {
        Box(Modifier.padding(24.dp)) {
            ContenidoDialogExito(
                titulo = "¡Solicitud enviada con éxito!",
                mensaje = "El equipo de CIM UC revisará tu requerimiento",
                onCerrar = {},
                textoBoton = "Entendido"
            )
        }
    }
}
