package cl.uc.cimuce.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Etiqueta
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoChip
import cl.uc.cimuce.ui.theme.VerdeTexto

/** Tarjeta de resultado de Explorar: imagen de 200dp, chip de tipo, año, título y "área · estado". */
@Composable
fun TarjetaResultado(
    proyecto: Proyecto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Image(
            painter = painterResource(proyecto.imagenRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChipTipo(proyecto.tipoContenido)
                Spacer(Modifier.weight(1f))
                Text(
                    text = proyecto.anoEjecucion.toString(),
                    fontFamily = SansCuerpo,
                    fontSize = 15.sp,
                    color = Etiqueta
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = proyecto.titulo,
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 23.sp,
                color = VerdeTexto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${proyecto.areaTematica} · ${proyecto.estadoProyecto}",
                fontFamily = SansCuerpo,
                fontSize = 14.sp,
                color = TextoChip
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE, widthDp = 360)
@Composable
private fun TarjetaResultadoPreview() {
    CIMUCETheme {
        TarjetaResultado(ProyectoRepository.getPorId(14)!!, {}, Modifier.padding(20.dp))
    }
}
