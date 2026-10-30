package cl.uc.cimuce.ui.screens.detalle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.Integrante
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.AvatarCirculo
import cl.uc.cimuce.ui.components.DialogExito
import cl.uc.cimuce.ui.components.EtiquetaSeccion
import cl.uc.cimuce.ui.components.TarjetaRecienteHorizontal
import cl.uc.cimuce.ui.theme.BordeChip
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario
import cl.uc.cimuce.ui.theme.VerdeTexto
import kotlinx.coroutines.launch

/**
 * Relaciones de un integrante (Req F3) en un bottom sheet, sin agregar niveles de navegación.
 * Al elegir otra iniciativa, el sheet se cierra y se llama a onAbrirIniciativa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetRelaciones(
    integrante: Integrante,
    indiceColor: Int,
    iniciativas: List<Proyecto>,
    idProyectoActual: Int,
    onCerrar: () -> Unit,
    onAbrirIniciativa: (Int) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val scope = rememberCoroutineScope()
    var mostrarExito by rememberSaveable { mutableStateOf(false) }

    // Anima el cierre antes de quitar el sheet de la composición.
    fun cerrarY(accion: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion { accion() }
    }

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Crema
    ) {
        ContenidoRelaciones(
            integrante = integrante,
            indiceColor = indiceColor,
            iniciativas = iniciativas,
            idProyectoActual = idProyectoActual,
            onContactar = { mostrarExito = true },
            onCerrar = { cerrarY(onCerrar) },
            onAbrirIniciativa = { id -> cerrarY { onAbrirIniciativa(id) } }
        )
    }

    if (mostrarExito) {
        DialogExito(
            titulo = "¡Solicitud enviada con éxito!",
            mensaje = "Tu solicitud a ${integrante.nombreCorto} fue enviada",
            onCerrar = { mostrarExito = false }
        )
    }
}

@Composable
private fun ContenidoRelaciones(
    integrante: Integrante,
    indiceColor: Int,
    iniciativas: List<Proyecto>,
    idProyectoActual: Int,
    onContactar: () -> Unit,
    onCerrar: () -> Unit,
    onAbrirIniciativa: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarCirculo(integrante, indiceColor, tamano = 88.dp)
                Spacer(Modifier.height(14.dp))
                Text(
                    text = integrante.nombreCompleto,
                    fontFamily = SerifTitulos,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = VerdeTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(integrante.rolCargo, fontFamily = SansCuerpo, fontSize = 15.sp, color = TextoCuerpo)
                Text(
                    integrante.organizacionAsociada,
                    fontFamily = SansCuerpo,
                    fontSize = 14.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onContactar,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Caramelo, contentColor = Color.White)
                    ) {
                        Text("Contactar", fontFamily = SansCuerpo, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onCerrar,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BordeChip),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoCuerpo)
                    ) {
                        Text("Cerrar", fontFamily = SansCuerpo, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
            EtiquetaSeccion("Participa en ${iniciativas.size} iniciativas")
        }
        items(iniciativas, key = { it.idProyecto }) { p ->
            TarjetaRecienteHorizontal(
                proyecto = p,
                onClick = { onAbrirIniciativa(p.idProyecto) },
                viendoAhora = p.idProyecto == idProyectoActual
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F4EE, widthDp = 360, heightDp = 900)
@Composable
private fun RelacionesPreview() {
    CIMUCETheme {
        ContenidoRelaciones(
            integrante = ProyectoRepository.getIntegrante(1)!!,
            indiceColor = 0,
            iniciativas = ProyectoRepository.getProyectosDeIntegrante(1),
            idProyectoActual = 1,
            onContactar = {}, onCerrar = {}, onAbrirIniciativa = {}
        )
    }
}
