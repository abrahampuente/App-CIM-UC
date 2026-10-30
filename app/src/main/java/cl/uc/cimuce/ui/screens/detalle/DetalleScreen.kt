package cl.uc.cimuce.ui.screens.detalle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uc.cimuce.data.model.Integrante
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.AvatarIntegrante
import cl.uc.cimuce.ui.components.ChipEstado
import cl.uc.cimuce.ui.components.DialogExito
import cl.uc.cimuce.ui.components.EtiquetaSeccion
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario
import cl.uc.cimuce.ui.theme.TextoSobreVerde
import cl.uc.cimuce.ui.theme.VerdeOscuro
import cl.uc.cimuce.ui.theme.VerdeTexto

private val MargenLateral = 20.dp

@Composable
fun DetalleScreen(
    favoritos: Set<Int>,
    onToggleFavorito: (Int) -> Unit,
    onVolver: () -> Unit,
    onAbrirIniciativa: (Int) -> Unit,
    viewModel: DetalleViewModel = viewModel()
) {
    val proyecto by viewModel.proyecto.collectAsStateWithLifecycle()
    val integrantes by viewModel.integrantes.collectAsStateWithLifecycle()

    val p = proyecto
    if (p == null) {
        NoEncontrado(onVolver)
        return
    }
    DetalleContenido(
        proyecto = p,
        integrantes = integrantes,
        favorito = p.idProyecto in favoritos,
        onToggleFavorito = { onToggleFavorito(p.idProyecto) },
        onVolver = onVolver,
        iniciativasDe = viewModel::iniciativasDe,
        onAbrirIniciativa = onAbrirIniciativa
    )
}

@OptIn(ExperimentalMaterial3Api::class) // SheetRelaciones usa ModalBottomSheet
@Composable
fun DetalleContenido(
    proyecto: Proyecto,
    integrantes: List<Integrante>,
    favorito: Boolean,
    onToggleFavorito: () -> Unit,
    onVolver: () -> Unit,
    iniciativasDe: (Int) -> List<Proyecto>,
    onAbrirIniciativa: (Int) -> Unit
) {
    var mostrarExito by rememberSaveable { mutableStateOf(false) }
    var idIntegranteAbierto by rememberSaveable { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Crema),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { Hero(proyecto, favorito, onVolver, onToggleFavorito) }

        item {
            Column(Modifier.padding(horizontal = MargenLateral).padding(top = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = proyecto.areaTematica,
                        fontFamily = SansCuerpo,
                        fontSize = 14.sp,
                        color = TextoSecundario,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = proyecto.anoEjecucion.toString(),
                        fontFamily = SansCuerpo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextoCuerpo
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = proyecto.titulo,
                    fontFamily = SerifTitulos,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    color = VerdeTexto,
                    modifier = Modifier.semantics { heading() }
                )
                Spacer(Modifier.height(12.dp))
                ChipEstado(proyecto.estadoProyecto)

                Divisor()
                EtiquetaSeccion("Descripción")
                Spacer(Modifier.height(12.dp))
                Text(
                    text = proyecto.descripcionDetallada,
                    fontFamily = SansCuerpo,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    color = TextoCuerpo
                )

                Divisor()
                EtiquetaSeccion("Integrantes asociados")
                Spacer(Modifier.height(14.dp))
            }
        }

        // Fuera del padding para que el carrusel llegue a los bordes.
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = MargenLateral),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(integrantes, key = { _, it -> it.idIntegrante }) { i, integrante ->
                    AvatarIntegrante(integrante, i, onClick = { idIntegranteAbierto = integrante.idIntegrante })
                }
            }
        }

        item {
            Text(
                text = "Toca un integrante para ver sus relaciones",
                fontFamily = SansCuerpo,
                fontSize = 14.sp,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = MargenLateral, vertical = 12.dp)
            )
        }

        item { TarjetaColaborar(onContactar = { mostrarExito = true }) }
    }

    if (mostrarExito) {
        DialogExito(
            titulo = "¡Solicitud enviada con éxito!",
            mensaje = "El equipo de CIM UC revisará tu requerimiento",
            onCerrar = { mostrarExito = false }
        )
    }

    // Relaciones del integrante (F3): bottom sheet en vez de una pantalla nueva (NF2).
    val indice = integrantes.indexOfFirst { it.idIntegrante == idIntegranteAbierto }
    if (indice >= 0) {
        val integrante = integrantes[indice]
        SheetRelaciones(
            integrante = integrante,
            indiceColor = indice,
            iniciativas = iniciativasDe(integrante.idIntegrante),
            idProyectoActual = proyecto.idProyecto,
            onCerrar = { idIntegranteAbierto = null },
            onAbrirIniciativa = { id ->
                idIntegranteAbierto = null
                onAbrirIniciativa(id)
            }
        )
    }
}

@Composable
private fun Divisor() {
    HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = BordeSuave)
}

/** Imagen de 300dp extendida bajo la status bar, con botones y chip de tipo superpuestos. */
@Composable
private fun Hero(
    proyecto: Proyecto,
    favorito: Boolean,
    onVolver: () -> Unit,
    onToggleFavorito: () -> Unit
) {
    Box(Modifier.fillMaxWidth().height(300.dp)) {
        Image(
            painter = painterResource(proyecto.imagenRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Sombra suave arriba para que los botones y la status bar se lean sobre fotos claras.
        Box(
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.35f), Color.Transparent)))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BotonHero(Icons.AutoMirrored.Filled.ArrowBack, "Volver", onVolver)
            BotonHero(
                icono = if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                descripcion = if (favorito) "Quitar de favoritos" else "Agregar a favoritos",
                onClick = onToggleFavorito
            )
        }
        Text(
            text = proyecto.tipoContenido.etiqueta,
            fontFamily = SansCuerpo,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
                .background(Caramelo, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun BotonHero(icono: ImageVector, descripcion: String, onClick: () -> Unit) {
    val forma = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(forma)
            .background(Color.White.copy(alpha = 0.25f))
            .border(1.dp, Color.White.copy(alpha = 0.4f), forma)
            .clickable(onClickLabel = descripcion, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = descripcion, tint = Color.White)
    }
}

/** CTA de colaboración (Req F4). */
@Composable
private fun TarjetaColaborar(onContactar: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(MargenLateral)
            .fillMaxWidth()
            .background(VerdeOscuro, RoundedCornerShape(20.dp))
            .padding(22.dp)
    ) {
        Text(
            text = "¿Interesado en colaborar?",
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Contacta al equipo de CIM UC para oportunidades de colaboración",
            fontFamily = SansCuerpo,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = TextoSobreVerde
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onContactar,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Caramelo, contentColor = Color.White)
        ) {
            Text("Contactar →", fontFamily = SansCuerpo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
private fun NoEncontrado(onVolver: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Crema).statusBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No encontramos este contenido", fontFamily = SerifTitulos, fontSize = 22.sp, color = TextoCuerpo)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onVolver, colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)) {
            Text("Volver", fontFamily = SansCuerpo)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
private fun DetallePreview() {
    CIMUCETheme {
        DetalleContenido(
            proyecto = ProyectoRepository.getPorId(1)!!,
            integrantes = ProyectoRepository.getIntegrantesDe(1),
            favorito = true,
            onToggleFavorito = {}, onVolver = {},
            iniciativasDe = ProyectoRepository::getProyectosDeIntegrante,
            onAbrirIniciativa = {}
        )
    }
}
