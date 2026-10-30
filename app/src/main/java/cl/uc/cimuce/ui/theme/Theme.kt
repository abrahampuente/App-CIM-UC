package cl.uc.cimuce.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Esquema fijo de la marca: sin dynamic color ni modo oscuro. */
private val EsquemaCimUc = lightColorScheme(
    primary = VerdeOscuro,
    onPrimary = Blanco,
    primaryContainer = ChipVerdeFondo,
    onPrimaryContainer = ChipVerdeTexto,
    secondary = Caramelo,
    onSecondary = Blanco,
    secondaryContainer = ChipCafeFondo,
    onSecondaryContainer = ChipCafeTexto,
    tertiary = VerdeClaro,
    onTertiary = Blanco,
    background = Crema,
    onBackground = TextoCuerpo,
    surface = Blanco,
    onSurface = TextoCuerpo,
    surfaceVariant = Crema,
    onSurfaceVariant = TextoSecundario,
    outline = BordeChip,
    outlineVariant = BordeSuave
)

@Composable
fun CIMUCETheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaCimUc,
        typography = Typography,
        content = content
    )
}
