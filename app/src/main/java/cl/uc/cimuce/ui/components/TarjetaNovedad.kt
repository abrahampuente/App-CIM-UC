package cl.uc.cimuce.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.Novedad
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Link
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario

private val EstiloResumen = TextStyle(fontFamily = SansCuerpo, fontSize = 14.sp, lineHeight = 21.sp, color = TextoCuerpo)

/**
 * Tarjeta de novedad: imagen de 160dp, título y fecha.
 * Con mostrarResumen, agrega el resumen en 2 líneas; si además está expandida,
 * lo muestra completo con AnimatedVisibility (sin navegar).
 */
@Composable
fun TarjetaNovedad(
    novedad: Novedad,
    modifier: Modifier = Modifier,
    mostrarResumen: Boolean = false,
    expandida: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val forma = RoundedCornerShape(18.dp)
    val colores = CardDefaults.cardColors(containerColor = Blanco)
    val borde = BorderStroke(1.dp, BordeSuave)
    val contenido: @Composable () -> Unit = {
        Column {
            Image(
                painter = painterResource(novedad.imagen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )
            Column(Modifier.padding(18.dp)) {
                Text(
                    text = novedad.titulo,
                    fontFamily = SansCuerpo,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextoCuerpo,
                    maxLines = if (expandida) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = novedad.fecha,
                    fontFamily = SansCuerpo,
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
                if (mostrarResumen) {
                    Spacer(Modifier.height(10.dp))
                    AnimatedVisibility(
                        visible = !expandida,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(novedad.resumen, style = EstiloResumen, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    AnimatedVisibility(
                        visible = expandida,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(novedad.resumen, style = EstiloResumen)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (expandida) "Leer menos" else "Leer más",
                        fontFamily = SansCuerpo,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Link
                    )
                }
            }
        }
    }
    if (onClick != null) {
        Card(onClick, modifier.fillMaxWidth(), shape = forma, colors = colores, border = borde) { contenido() }
    } else {
        Card(modifier.fillMaxWidth(), shape = forma, colors = colores, border = borde) { contenido() }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE, widthDp = 360, heightDp = 900)
@Composable
private fun TarjetaNovedadPreview() {
    CIMUCETheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TarjetaNovedad(ProyectoRepository.getNovedades()[0], mostrarResumen = true)
            TarjetaNovedad(ProyectoRepository.getNovedades()[1], mostrarResumen = true, expandida = true)
        }
    }
}
