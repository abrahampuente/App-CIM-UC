package cl.uc.cimuce.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.uc.cimuce.ui.components.IconosBarraEstado
import cl.uc.cimuce.ui.navigation.AppNavHost
import cl.uc.cimuce.ui.navigation.BarraInferior
import cl.uc.cimuce.ui.navigation.Pestana
import cl.uc.cimuce.ui.navigation.Rutas
import cl.uc.cimuce.ui.navigation.irAPestana
import cl.uc.cimuce.ui.theme.Crema

@Composable
fun CimUcApp(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()

    // Pestaña activa según la jerarquía del destino actual. El detalle no pertenece a ninguna
    // pestaña: hereda la de la pantalla desde la que se abrió (siempre está justo debajo, NF2).
    fun pestanaDe(destino: NavDestination?) = Pestana.entries.firstOrNull { p ->
        destino?.hierarchy?.any { it.route == p.ruta } == true
    }
    val pestanaActiva = entradaActual?.let {
        pestanaDe(it.destination) ?: pestanaDe(navController.previousBackStackEntry?.destination)
    }
    val rutaActual = entradaActual?.destination?.route
    val mostrarBarra = rutaActual != null && rutaActual != Rutas.ONBOARDING

    // Onboarding e Inicio (header verde) y Detalle (imagen) se extienden bajo la status bar.
    IconosBarraEstado(claros = rutaActual in setOf(Rutas.ONBOARDING, Rutas.INICIO, Rutas.DETALLE))

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Crema,
        // Sin insets del sistema: cada pantalla decide si se extiende bajo la status bar.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(
                    pestanaActiva = pestanaActiva,
                    onPestana = { navController.irAPestana(it, pestanaActiva) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            appViewModel = appViewModel,
            modifier = Modifier
                .padding(innerPadding)
                // En horizontal, la barra de navegación del sistema y el notch quedan a los lados.
                .windowInsetsPadding(
                    WindowInsets.navigationBars.union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Horizontal)
                )
        )
    }
}
