package cl.uc.cimuce.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.Etiqueta
import cl.uc.cimuce.ui.theme.Link
import cl.uc.cimuce.ui.theme.VerdeClaro
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.VerdeTexto

/**
 * Tarjeta horizontal de 112dp: imagen cuadrada a la izquierda; chip, año y título a la derecha.
 * Con viendoAhora = true queda deshabilitada y muestra "Viendo ahora" en lugar del año.
 * accion, si se entrega, se dibuja al extremo derecho (p. ej. quitar de guardados).
 */
@Composable
fun TarjetaRecienteHorizontal(
    proyecto: Proyecto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viendoAhora: Boolean = false,
    accion: (@Composable () -> Unit)? = null
) {
    Card(
        onClick = onClick,
        enabled = !viendoAhora,
        modifier = modifier.fillMaxWidth().height(112.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco, disabledContainerColor = Crema),
        border = BorderStroke(1.dp, if (viendoAhora) VerdeClaro else BordeSuave)
    ) {
        Row(Modifier.alpha(if (viendoAhora) 0.65f else 1f)) {
            Image(
                painter = painterResource(proyecto.imagenRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(112.dp)
            )
            Column(Modifier.weight(1f).padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChipTipo(proyecto.tipoContenido)
                    Spacer(Modifier.weight(1f))
                    if (viendoAhora) {
                        Text(
                            text = "Viendo ahora",
                            fontFamily = SansCuerpo,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Link
                        )
                    } else {
                        Text(
                            text = proyecto.anoEjecucion.toString(),
                            fontFamily = SansCuerpo,
                            fontSize = 15.sp,
                            color = Etiqueta
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = proyecto.titulo,
                    fontFamily = SerifTitulos,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    color = VerdeTexto,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (accion != null) {
                Box(Modifier.fillMaxHeight().padding(end = 4.dp), contentAlignment = Alignment.Center) {
                    accion()
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE, widthDp = 360)
@Composable
private fun TarjetaRecienteHorizontalPreview() {
    CIMUCETheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaRecienteHorizontal(ProyectoRepository.getRecientes(1).first(), {})
            TarjetaRecienteHorizontal(ProyectoRepository.getPorId(13)!!, {}, viendoAhora = true)
        }
    }
}
