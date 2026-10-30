package cl.uc.cimuce.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.VerdeTexto

/** Tarjeta vertical de 220dp para el carrusel de recomendados (Matchmaking). */
@Composable
fun TarjetaRecomendada(
    proyecto: Proyecto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(220.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Image(
            painter = painterResource(proyecto.imagenRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        Column(Modifier.padding(14.dp)) {
            ChipTipo(proyecto.tipoContenido)
            Spacer(Modifier.height(8.dp))
            Text(
                text = proyecto.titulo,
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                color = VerdeTexto,
                minLines = 2, // alto uniforme en el carrusel
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE)
@Composable
private fun TarjetaRecomendadaPreview() {
    CIMUCETheme {
        TarjetaRecomendada(ProyectoRepository.getTodos().first(), {}, Modifier.padding(20.dp))
    }
}
