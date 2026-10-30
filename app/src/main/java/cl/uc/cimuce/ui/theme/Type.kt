package cl.uc.cimuce.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.R

/**
 * Títulos: DM Serif Display. La familia solo trae peso Regular (ya es de trazo grueso),
 * así que se registra también como Bold para evitar el negrita sintético.
 */
val SerifTitulos = FontFamily(
    Font(R.font.dm_serif_display, FontWeight.Normal),
    Font(R.font.dm_serif_display, FontWeight.Bold)
)

/** Cuerpo: Inter variable (res/font/inter.ttf); cada peso fija el eje wght. */
@OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
private fun inter(peso: FontWeight) = Font(
    R.font.inter,
    weight = peso,
    variationSettings = FontVariation.Settings(FontVariation.weight(peso.weight))
)

val SansCuerpo = FontFamily(
    inter(FontWeight.Normal),
    inter(FontWeight.Medium),
    inter(FontWeight.SemiBold),
    inter(FontWeight.Bold)
)

private val base = Typography()

private fun TextStyle.titulo() = copy(fontFamily = SerifTitulos, fontWeight = FontWeight.Bold, color = VerdeTexto)
private fun TextStyle.cuerpo() = copy(fontFamily = SansCuerpo)

val Typography = Typography(
    displayLarge = base.displayLarge.titulo(),
    displayMedium = base.displayMedium.titulo(),
    displaySmall = base.displaySmall.titulo(),
    headlineLarge = base.headlineLarge.titulo(),
    headlineMedium = base.headlineMedium.titulo(),
    headlineSmall = base.headlineSmall.titulo(),
    titleLarge = base.titleLarge.titulo(),
    titleMedium = base.titleMedium.cuerpo().copy(fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.cuerpo().copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge.cuerpo(),
    bodyMedium = base.bodyMedium.cuerpo(),
    // Mínimo de 13sp en toda la app (accesibilidad): se suben los estilos pequeños de Material,
    // que usan p. ej. los textos de ayuda y las etiquetas flotantes de los campos.
    bodySmall = base.bodySmall.cuerpo().copy(fontSize = 13.sp, lineHeight = 18.sp),
    labelLarge = base.labelLarge.cuerpo(),
    labelMedium = base.labelMedium.cuerpo().copy(fontSize = 13.sp, lineHeight = 18.sp),
    labelSmall = base.labelSmall.cuerpo().copy(fontSize = 13.sp, lineHeight = 18.sp)
)

/** Etiqueta de sección: aplicar junto con text.uppercase() (TextStyle no transforma a mayúsculas). */
val EstiloEtiquetaSeccion = TextStyle(
    fontFamily = SansCuerpo,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    letterSpacing = 1.5.sp,
    color = Etiqueta
)
