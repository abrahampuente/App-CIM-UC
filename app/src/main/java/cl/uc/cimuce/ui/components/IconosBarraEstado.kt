package cl.uc.cimuce.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Íconos de la status bar: claros sobre headers verdes que se extienden bajo ella,
 * oscuros sobre fondo crema.
 */
@Composable
fun IconosBarraEstado(claros: Boolean) {
    if (LocalInspectionMode.current) return
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !claros
    }
}
