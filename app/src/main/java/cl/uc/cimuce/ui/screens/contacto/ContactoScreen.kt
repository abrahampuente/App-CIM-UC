package cl.uc.cimuce.ui.screens.contacto

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.repository.ProyectoRepository
import cl.uc.cimuce.ui.components.DialogExito
import cl.uc.cimuce.ui.components.EncabezadoPestana
import cl.uc.cimuce.ui.components.EtiquetaSeccion
import cl.uc.cimuce.ui.components.TarjetaRecienteHorizontal
import cl.uc.cimuce.ui.theme.Blanco
import cl.uc.cimuce.ui.theme.BordeChip
import cl.uc.cimuce.ui.theme.BordeSuave
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.Crema
import cl.uc.cimuce.ui.theme.Etiqueta
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.TextoCuerpo
import cl.uc.cimuce.ui.theme.TextoSecundario
import cl.uc.cimuce.ui.theme.TextoSobreVerde
import cl.uc.cimuce.ui.theme.VerdeOscuro

@Composable
fun ContactoScreen(
    perfilOnboarding: PerfilUsuario?,
    favoritos: Set<Int>,
    onQuitarFavorito: (Int) -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    viewModel: ContactoViewModel = viewModel()
) {
    LaunchedEffect(perfilOnboarding) { viewModel.precargarPerfil(perfilOnboarding) }
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()
    // Orden de guardado: el más reciente primero.
    val guardados = remember(favoritos) {
        favoritos.reversed().mapNotNull(ProyectoRepository::getPorId)
    }

    ContactoContenido(
        formulario = formulario,
        guardados = guardados,
        onNombre = viewModel::cambiarNombre,
        onOrganizacion = viewModel::cambiarOrganizacion,
        onPerfil = viewModel::cambiarPerfil,
        onMensaje = viewModel::cambiarMensaje,
        onEnviar = viewModel::enviar,
        onQuitarFavorito = onQuitarFavorito,
        onAbrirDetalle = onAbrirDetalle
    )

    if (formulario.enviado) {
        DialogExito(
            titulo = "¡Solicitud enviada con éxito!",
            mensaje = "El equipo de CIM UC revisará tu requerimiento",
            onCerrar = viewModel::cerrarExito
        )
    }
}

@Composable
fun ContactoContenido(
    formulario: FormularioContacto,
    guardados: List<Proyecto>,
    onNombre: (String) -> Unit,
    onOrganizacion: (String) -> Unit,
    onPerfil: (PerfilUsuario) -> Unit,
    onMensaje: (String) -> Unit,
    onEnviar: () -> Unit,
    onQuitarFavorito: (Int) -> Unit,
    onAbrirDetalle: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Crema).statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { EncabezadoPestana("Contacto", "Conecta con el Centro UC de Innovación en Madera") }
        item { TarjetaDatosContacto() }
        item {
            Formulario(formulario, onNombre, onOrganizacion, onPerfil, onMensaje, onEnviar)
        }
        item { EtiquetaSeccion("Mis guardados", Modifier.padding(top = 8.dp)) }
        if (guardados.isEmpty()) {
            item {
                Text(
                    text = "Guarda proyectos con ♡ o con el Modo Feria",
                    fontFamily = SansCuerpo,
                    fontSize = 14.sp,
                    color = TextoSecundario
                )
            }
        } else {
            items(guardados, key = { it.idProyecto }) { p ->
                TarjetaRecienteHorizontal(
                    proyecto = p,
                    onClick = { onAbrirDetalle(p.idProyecto) },
                    modifier = Modifier.animateItem(),
                    accion = {
                        IconButton(onClick = { onQuitarFavorito(p.idProyecto) }) {
                            Icon(Icons.Filled.BookmarkRemove, contentDescription = "Quitar de guardados", tint = Etiqueta)
                        }
                    }
                )
            }
        }
    }
}

/** Datos ficticios del centro, con el estilo de la tarjeta CTA. */
@Composable
private fun TarjetaDatosContacto() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeOscuro, RoundedCornerShape(20.dp))
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Centro UC de Innovación en Madera",
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
        FilaDato(Icons.Filled.Email, "contacto@ejemplo-cimuc.cl")
        FilaDato(Icons.Filled.Phone, "+56 2 0000 0000")
        FilaDato(Icons.Filled.LocationOn, "Campus San Joaquín (referencial)")
    }
}

@Composable
private fun FilaDato(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = Caramelo, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(texto, fontFamily = SansCuerpo, fontSize = 15.sp, color = TextoSobreVerde)
    }
}

@Composable
private fun Formulario(
    formulario: FormularioContacto,
    onNombre: (String) -> Unit,
    onOrganizacion: (String) -> Unit,
    onPerfil: (PerfilUsuario) -> Unit,
    onMensaje: (String) -> Unit,
    onEnviar: () -> Unit
) {
    val forma = RoundedCornerShape(20.dp)
    val errores = formulario.mostrarErrores
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blanco, forma)
            .border(1.dp, BordeSuave, forma)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Envíanos tu solicitud",
            fontFamily = SerifTitulos,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TextoCuerpo
        )
        Campo("Nombre", formulario.nombre, onNombre, errores && formulario.nombreInvalido)
        Campo("Organización", formulario.organizacion, onOrganizacion, errores && formulario.organizacionInvalida)
        SelectorPerfil(formulario.perfil, onPerfil, errores && formulario.perfilInvalido)
        Campo(
            etiqueta = "Mensaje",
            valor = formulario.mensaje,
            onCambio = onMensaje,
            conError = errores && formulario.mensajeInvalido,
            multilinea = true
        )
        Button(
            onClick = onEnviar,
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(top = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Caramelo, contentColor = Color.White)
        ) {
            Text("Enviar solicitud", fontFamily = SansCuerpo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

private val EstiloCampo = TextStyle(fontFamily = SansCuerpo, fontSize = 15.sp, color = TextoCuerpo)

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = VerdeOscuro,
    unfocusedBorderColor = BordeChip,
    focusedLabelColor = VerdeOscuro,
    unfocusedLabelColor = TextoSecundario,
    cursorColor = VerdeOscuro,
    focusedContainerColor = Blanco,
    unfocusedContainerColor = Blanco
)

@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    conError: Boolean,
    multilinea: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, fontFamily = SansCuerpo) },
        isError = conError,
        supportingText = if (conError) {
            { Text("Este campo es obligatorio", fontFamily = SansCuerpo) }
        } else null,
        singleLine = !multilinea,
        minLines = if (multilinea) 4 else 1,
        textStyle = EstiloCampo,
        shape = RoundedCornerShape(12.dp),
        colors = coloresCampo(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = if (multilinea) ImeAction.Default else ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth().then(if (multilinea) Modifier.heightIn(min = 120.dp) else Modifier)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorPerfil(
    perfil: PerfilUsuario?,
    onPerfil: (PerfilUsuario) -> Unit,
    conError: Boolean
) {
    var abierto by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = it }) {
        OutlinedTextField(
            value = perfil?.etiqueta.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Perfil", fontFamily = SansCuerpo) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            isError = conError,
            supportingText = if (conError) {
                { Text("Elige un perfil", fontFamily = SansCuerpo) }
            } else null,
            textStyle = EstiloCampo,
            shape = RoundedCornerShape(12.dp),
            colors = coloresCampo(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false },
            containerColor = Blanco
        ) {
            PerfilUsuario.entries.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.etiqueta, fontFamily = SansCuerpo) },
                    onClick = {
                        onPerfil(opcion)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1600)
@Composable
private fun ContactoPreview() {
    CIMUCETheme {
        ContactoContenido(
            formulario = FormularioContacto(perfil = PerfilUsuario.ACADEMIA, mostrarErrores = true),
            guardados = listOf(ProyectoRepository.getPorId(1)!!, ProyectoRepository.getPorId(21)!!),
            onNombre = {}, onOrganizacion = {}, onPerfil = {}, onMensaje = {}, onEnviar = {},
            onQuitarFavorito = {}, onAbrirDetalle = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1200)
@Composable
private fun ContactoSinGuardadosPreview() {
    CIMUCETheme {
        ContactoContenido(
            formulario = FormularioContacto(),
            guardados = emptyList(),
            onNombre = {}, onOrganizacion = {}, onPerfil = {}, onMensaje = {}, onEnviar = {},
            onQuitarFavorito = {}, onAbrirDetalle = {}
        )
    }
}
