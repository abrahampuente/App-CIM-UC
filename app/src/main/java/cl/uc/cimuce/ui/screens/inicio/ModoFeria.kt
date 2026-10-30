package cl.uc.cimuce.ui.screens.inicio

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import kotlinx.coroutines.delay

private const val DURACION_ESCANEO_MS = 2_000L
private val FondoEscaner = Color(0xF20F1A13)

/**
 * Escáner QR simulado del Modo Feria. Tras 2 segundos llama a onEscaneado;
 * no usa la cámara (todo es en memoria, Req NF3).
 */
@Composable
fun DialogEscaneoFeria(onEscaneado: () -> Unit, onCancelar: () -> Unit) {
    val alEscanear by rememberUpdatedState(onEscaneado)
    LaunchedEffect(Unit) {
        delay(DURACION_ESCANEO_MS)
        alEscanear()
    }
    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ContenidoEscaner(onCancelar)
    }
}

@Composable
private fun ContenidoEscaner(onCancelar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(FondoEscaner, RoundedCornerShape(28.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Modo Feria",
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Apunta al código QR del stand",
            fontFamily = SansCuerpo,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(36.dp))
        RecuadroEscaner()
        Spacer(Modifier.height(28.dp))
        Text(
            text = "Escaneando código del stand...",
            fontFamily = SansCuerpo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(Modifier.height(36.dp))
        TextButton(onClick = onCancelar) {
            Text("Cancelar", fontFamily = SansCuerpo, color = Color.White.copy(alpha = 0.8f))
        }
    }
}

/** Recuadro de 240dp con esquinas caramelo y una línea que sube y baja. */
@Composable
private fun RecuadroEscaner() {
    val progreso = if (LocalInspectionMode.current) {
        0.5f
    } else {
        val transicion = rememberInfiniteTransition(label = "escaner")
        val valor by transicion.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1_100, easing = LinearEasing), RepeatMode.Reverse),
            label = "linea"
        )
        valor
    }
    Box(Modifier.size(240.dp)) {
        Canvas(Modifier.fillMaxSize()) {
            dibujarEsquinas()
            val margen = 14.dp.toPx()
            val y = margen + (size.height - 2 * margen) * progreso
            // Halo suave bajo la línea
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Caramelo.copy(alpha = 0.25f)),
                    startY = y - 40.dp.toPx(),
                    endY = y
                ),
                topLeft = Offset(margen, y - 40.dp.toPx()),
                size = Size(size.width - 2 * margen, 40.dp.toPx())
            )
            drawLine(
                color = Caramelo,
                start = Offset(margen, y),
                end = Offset(size.width - margen, y),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.dibujarEsquinas() {
    val grosor = 5.dp.toPx()
    val largo = 44.dp.toPx()
    val radio = 18.dp.toPx()
    val w = size.width
    val h = size.height
    // Marco tenue completo y esquinas marcadas encima.
    drawRoundRect(
        color = Color.White.copy(alpha = 0.12f),
        cornerRadius = CornerRadius(radio),
        style = Stroke(1.dp.toPx())
    )
    val esquinas = listOf(
        Offset(0f, 0f) to Pair(Offset(largo, 0f), Offset(0f, largo)),
        Offset(w, 0f) to Pair(Offset(w - largo, 0f), Offset(w, largo)),
        Offset(0f, h) to Pair(Offset(largo, h), Offset(0f, h - largo)),
        Offset(w, h) to Pair(Offset(w - largo, h), Offset(w, h - largo))
    )
    esquinas.forEach { (vertice, extremos) ->
        drawLine(Caramelo, vertice, extremos.first, grosor, StrokeCap.Round)
        drawLine(Caramelo, vertice, extremos.second, grosor, StrokeCap.Round)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun EscanerPreview() {
    CIMUCETheme { ContenidoEscaner(onCancelar = {}) }
}
