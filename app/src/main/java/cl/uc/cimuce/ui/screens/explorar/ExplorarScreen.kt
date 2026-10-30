package cl.uc.cimuce.ui.screens.explorar

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uc.cimuce.data.model.AreaTematica
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.ChipFiltro
import cl.uc.cimuce.ui.components.TarjetaResultado
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.BordeChip
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.Etiqueta
import cl.uc.cimuce.ui.theme.Link
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario

private val MargenLateral = 20.dp

@Composable
fun ExplorarScreen(
    mostrarVolver: Boolean,
    onVolver: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    viewModel: ExplorarViewModel = viewModel()
) {
    val tipo by viewModel.tipo.collectAsStateWithLifecycle()
    val area by viewModel.area.collectAsStateWithLifecycle()
    val texto by viewModel.texto.collectAsStateWithLifecycle()
    val resultados by viewModel.resultados.collectAsStateWithLifecycle()

    ExplorarContenido(
        tipo = tipo,
        area = area,
        texto = texto,
        resultados = resultados,
        mostrarVolver = mostrarVolver,
        onVolver = onVolver,
        onTexto = viewModel::cambiarTexto,
        onTipo = viewModel::elegirTipo,
        onArea = viewModel::alternarArea,
        onLimpiar = viewModel::limpiarFiltros,
        onAbrirDetalle = onAbrirDetalle
    )
}

@Composable
fun ExplorarContenido(
    tipo: TipoContenido?,
    area: String?,
    texto: String,
    resultados: List<Proyecto>,
    mostrarVolver: Boolean,
    onVolver: () -> Unit,
    onTexto: (String) -> Unit,
    onTipo: (TipoContenido?) -> Unit,
    onArea: (String) -> Unit,
    onLimpiar: () -> Unit,
    onAbrirDetalle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .statusBarsPadding()
    ) {
        // 1. Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (mostrarVolver) 8.dp else MargenLateral, end = MargenLateral, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mostrarVolver) {
                IconButton(onClick = onVolver) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextoCuerpo,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = tipo?.etiqueta ?: "Explorar",
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextoCuerpo,
                modifier = Modifier.semantics { heading() }
            )
        }
        Spacer(Modifier.height(14.dp))

        // 2. Buscador en vivo
        Buscador(texto, onTexto, Modifier.padding(horizontal = MargenLateral))
        Spacer(Modifier.height(6.dp))

        // 3. Chips por tipo (null = Todos)
        LazyRow(
            contentPadding = PaddingValues(horizontal = MargenLateral),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { ChipFiltro("Todos", seleccionado = tipo == null, onClick = { onTipo(null) }) }
            items(TipoContenido.entries) { t ->
                ChipFiltro(t.etiqueta, seleccionado = tipo == t, onClick = { onTipo(t) })
            }
        }

        // 4. Chips por área (opcionales, uno a la vez)
        LazyRow(
            contentPadding = PaddingValues(horizontal = MargenLateral),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(AreaTematica.todas) { a ->
                ChipFiltro(a, seleccionado = area == a, onClick = { onArea(a) }, pequeno = true)
            }
        }
        Spacer(Modifier.height(8.dp))

        // 5. Divider y contador
        HorizontalDivider(Modifier.padding(horizontal = MargenLateral), thickness = 1.dp, color = BordeSuave)
        Text(
            text = if (resultados.size == 1) "1 resultado" else "${resultados.size} resultados",
            fontFamily = SansCuerpo,
            fontSize = 14.sp,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = MargenLateral, vertical = 12.dp)
        )

        // 6-7. Resultados o estado vacío
        if (resultados.isEmpty()) {
            EstadoVacio(onLimpiar, Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = MargenLateral, end = MargenLateral, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(resultados, key = { it.idProyecto }) { p ->
                    TarjetaResultado(p, onClick = { onAbrirDetalle(p.idProyecto) })
                }
            }
        }
    }
}

@Composable
private fun Buscador(texto: String, onTexto: (String) -> Unit, modifier: Modifier = Modifier) {
    val foco = LocalFocusManager.current
    val forma = RoundedCornerShape(14.dp)
    BasicTextField(
        value = texto,
        onValueChange = onTexto,
        singleLine = true,
        textStyle = TextStyle(fontFamily = SansCuerpo, fontSize = 15.sp, color = TextoCuerpo),
        cursorBrush = SolidColor(TextoCuerpo),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
        modifier = modifier.fillMaxWidth().semantics { contentDescription = "Buscar por título, área o tema" },
        decorationBox = { campo ->
            Row(
                modifier = Modifier
                    .height(52.dp)
                    .background(Blanco, forma)
                    .border(1.dp, BordeChip, forma)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Etiqueta)
                Spacer(Modifier.width(10.dp))
                Box(Modifier.weight(1f)) {
                    if (texto.isEmpty()) {
                        Text(
                            text = "Buscar por título, área o tema...",
                            fontFamily = SansCuerpo,
                            fontSize = 15.sp,
                            color = TextoSecundario,
                            maxLines = 1
                        )
                    }
                    campo()
                }
            }
        }
    )
}

@Composable
private fun EstadoVacio(onLimpiar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.SearchOff, contentDescription = null, tint = Etiqueta, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(14.dp))
        Text(
            text = "No encontramos resultados",
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TextoCuerpo,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        TextButton(onClick = onLimpiar) {
            Text("Limpiar filtros", fontFamily = SansCuerpo, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Link)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ExplorarPreview() {
    CIMUCETheme {
        ExplorarContenido(
            tipo = TipoContenido.PROYECTO,
            area = null,
            texto = "",
            resultados = ProyectoRepository.getPorTipo(TipoContenido.PROYECTO),
            mostrarVolver = true,
            onVolver = {}, onTexto = {}, onTipo = {}, onArea = {}, onLimpiar = {}, onAbrirDetalle = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ExplorarVacioPreview() {
    CIMUCETheme {
        ExplorarContenido(
            tipo = null,
            area = AreaTematica.NORMALIZACION,
            texto = "xyz",
            resultados = emptyList(),
            mostrarVolver = false,
            onVolver = {}, onTexto = {}, onTipo = {}, onArea = {}, onLimpiar = {}, onAbrirDetalle = {}
        )
    }
}
