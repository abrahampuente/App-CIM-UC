package cl.uc.cimuce.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

/** Pestañas de la barra inferior. ruta es el patrón registrado en el NavHost. */
enum class Pestana(
    val ruta: String,
    val etiqueta: String,
    val iconoActivo: ImageVector,
    val iconoInactivo: ImageVector
) {
    INICIO(Rutas.INICIO, "Inicio", Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORAR(Rutas.EXPLORAR, "Explorar", Icons.Filled.Search, Icons.Outlined.Search),
    NOVEDADES(Rutas.NOVEDADES, "Novedades", Icons.Filled.Newspaper, Icons.Outlined.Newspaper),
    CONTACTO(Rutas.CONTACTO, "Contacto", Icons.Filled.Mail, Icons.Outlined.Mail)
}
