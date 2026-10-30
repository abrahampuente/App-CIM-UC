package cl.uc.cimuce.ui.screens.novedades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uc.cimuce.data.model.Novedad
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.EncabezadoPestana
import cl.uc.cimuce.ui.components.TarjetaNovedad
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Crema

@Composable
fun NovedadesScreen(viewModel: NovedadesViewModel = viewModel()) {
    val novedades by viewModel.novedades.collectAsStateWithLifecycle()
    val expandidas by viewModel.expandidas.collectAsStateWithLifecycle()
    NovedadesContenido(novedades, expandidas, viewModel::alternar)
}

@Composable
fun NovedadesContenido(
    novedades: List<Novedad>,
    expandidas: Set<Int>,
    onAlternar: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Crema).statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoPestana("Novedades", "Actividad reciente del CIM UC", Modifier.padding(bottom = 6.dp))
        }
        items(novedades, key = { it.idNovedad }) { n ->
            TarjetaNovedad(
                novedad = n,
                mostrarResumen = true,
                expandida = n.idNovedad in expandidas,
                onClick = { onAlternar(n.idNovedad) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun NovedadesPreview() {
    CIMUCETheme {
        NovedadesContenido(ProyectoRepository.getNovedades(), setOf(1), onAlternar = {})
    }
}
