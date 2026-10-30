package cl.uc.cimuce.ui.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.DialogExito
import cl.uc.cimuce.ui.components.EtiquetaSeccion
import cl.uc.cimuce.ui.components.LogoCimUc
import cl.uc.cimuce.ui.components.TarjetaCategoria
import cl.uc.cimuce.ui.components.TarjetaNovedad
import cl.uc.cimuce.ui.components.TarjetaRecienteHorizontal
import cl.uc.cimuce.ui.components.TarjetaRecomendada
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Cafe
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.Link
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.VerdeClaro
import cl.uc.cimuce.ui.theme.VerdeMedio
import cl.uc.cimuce.ui.theme.VerdeOscuro

private val MargenLateral = 20.dp
private val EspacioEtiqueta = 14.dp

@Composable
fun InicioScreen(
    perfil: PerfilUsuario?,
    onBuscar: (String) -> Unit,
    onAbrirTipo: (TipoContenido) -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    onVerNovedades: () -> Unit,
    onEscanearFeria: () -> Proyecto?,
    viewModel: InicioViewModel = viewModel()
) {
    LaunchedEffect(perfil) { viewModel.actualizarPerfil(perfil) }
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // Modo Feria: escaneando → resultado (título guardado, o "" si ya no quedan ítems).
    var escaneando by rememberSaveable { mutableStateOf(false) }
    var tituloGuardado by rememberSaveable { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {
        InicioContenido(estado, onBuscar, onAbrirTipo, onAbrirDetalle, onVerNovedades)
        FloatingActionButton(
            onClick = { escaneando = true },
            containerColor = Caramelo,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Filled.QrCodeScanner, contentDescription = "Modo Feria: escanear QR")
        }
    }

    if (escaneando) {
        DialogEscaneoFeria(
            onEscaneado = {
                escaneando = false
                tituloGuardado = onEscanearFeria()?.titulo.orEmpty()
            },
            onCancelar = { escaneando = false }
        )
    }
    tituloGuardado?.let { titulo ->
        DialogExito(
            titulo = if (titulo.isNotEmpty()) "¡Proyecto guardado!" else "Todo guardado",
            mensaje = if (titulo.isNotEmpty()) "$titulo se agregó a Mis guardados"
            else "Ya tienes todos los proyectos en Mis guardados",
            onCerrar = { tituloGuardado = null }
        )
    }
}

@Composable
fun InicioContenido(
    estado: InicioUiState,
    onBuscar: (String) -> Unit,
    onAbrirTipo: (TipoContenido) -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    onVerNovedades: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Crema),
        // Espacio extra al final para que el FAB del Modo Feria no tape la última tarjeta.
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        item { Header(onBuscar) }

        // 2. Matchmaking: recomendados según el perfil elegido en el onboarding.
        if (estado.perfil != null && estado.recomendados.isNotEmpty()) {
            item {
                Column {
                    EtiquetaSeccion(
                        "Recomendados para ti · ${estado.perfil.etiqueta}",
                        Modifier.padding(horizontal = MargenLateral)
                    )
                    Spacer(Modifier.height(EspacioEtiqueta))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = MargenLateral),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.recomendados, key = { it.idProyecto }) { p ->
                            TarjetaRecomendada(p, onClick = { onAbrirDetalle(p.idProyecto) })
                        }
                    }
                }
            }
        }

        // 3. Categorías por tipo de contenido.
        item {
            Seccion("Explorar por categoría") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaCategoria(
                        titulo = "Proyectos",
                        subtitulo = "${estado.conteos[TipoContenido.PROYECTO] ?: 0} proyectos activos",
                        icono = Icons.Filled.Architecture,
                        fondo = VerdeOscuro,
                        onClick = { onAbrirTipo(TipoContenido.PROYECTO) },
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaCategoria(
                        titulo = "Capacidades I+D",
                        subtitulo = "${estado.conteos[TipoContenido.CAPACIDAD_ID] ?: 0} líneas de trabajo",
                        icono = Icons.Filled.Biotech,
                        fondo = Cafe,
                        onClick = { onAbrirTipo(TipoContenido.CAPACIDAD_ID) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(12.dp))
                TarjetaCategoria(
                    titulo = "Publicaciones",
                    subtitulo = "${estado.conteos[TipoContenido.PUBLICACION] ?: 0} documentos",
                    icono = Icons.Filled.Description,
                    fondo = VerdeMedio,
                    onClick = { onAbrirTipo(TipoContenido.PUBLICACION) },
                    horizontal = true
                )
            }
        }

        // 4. Novedad destacada.
        estado.novedadDestacada?.let { novedad ->
            item {
                Column(Modifier.padding(horizontal = MargenLateral)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EtiquetaSeccion("Novedades", Modifier.weight(1f))
                        Text(
                            text = "Ver todo →",
                            fontFamily = SansCuerpo,
                            fontSize = 14.sp,
                            color = Link,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(6.dp))
                                .clickable(onClick = onVerNovedades)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.height(EspacioEtiqueta))
                    TarjetaNovedad(novedad)
                }
            }
        }

        // 5. Proyectos recientes.
        item {
            Seccion("Proyectos recientes") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    estado.recientes.forEach { p ->
                        TarjetaRecienteHorizontal(p, onClick = { onAbrirDetalle(p.idProyecto) })
                    }
                }
            }
        }
    }
}

@Composable
private fun Seccion(etiqueta: String, contenido: @Composable () -> Unit) {
    Column(Modifier.padding(horizontal = MargenLateral)) {
        EtiquetaSeccion(etiqueta)
        Spacer(Modifier.height(EspacioEtiqueta))
        contenido()
    }
}

/** Header verde que se extiende bajo la status bar, con logo y buscador. */
@Composable
private fun Header(onBuscar: (String) -> Unit) {
    var texto by rememberSaveable { mutableStateOf("") }
    val foco = LocalFocusManager.current
    val buscar = {
        foco.clearFocus()
        onBuscar(texto.trim())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(VerdeOscuro, VerdeClaro)))
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        LogoCimUc()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Explora proyectos, capacidades y publicaciones",
            fontFamily = SansCuerpo,
            fontSize = 15.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BasicTextField(
                value = texto,
                onValueChange = { texto = it },
                singleLine = true,
                textStyle = TextStyle(fontFamily = SansCuerpo, fontSize = 15.sp, color = Color.White),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { buscar() }),
                modifier = Modifier.weight(1f).semantics { contentDescription = "Buscar proyectos, capacidades y publicaciones" },
                decorationBox = { campo ->
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.width(10.dp))
                        Box(Modifier.weight(1f)) {
                            if (texto.isEmpty()) {
                                Text(
                                    text = "Buscar proyectos, capacidades...",
                                    fontFamily = SansCuerpo,
                                    fontSize = 15.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    maxLines = 1
                                )
                            }
                            campo()
                        }
                    }
                }
            )
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Caramelo)
                    .clickable(onClick = buscar),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Buscar",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1800)
@Composable
private fun InicioPreview() {
    CIMUCETheme {
        InicioContenido(
            estado = InicioUiState(
                perfil = PerfilUsuario.EMPRESA,
                recomendados = ProyectoRepository.getRecomendados(PerfilUsuario.EMPRESA),
                conteos = TipoContenido.entries.associateWith { ProyectoRepository.contarPorTipo(it) },
                novedadDestacada = ProyectoRepository.getNovedades().first(),
                recientes = ProyectoRepository.getRecientes(3)
            ),
            onBuscar = {}, onAbrirTipo = {}, onAbrirDetalle = {}, onVerNovedades = {}
        )
    }
}
