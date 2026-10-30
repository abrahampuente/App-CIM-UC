package cl.uc.cimuce.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import cl.uc.cimuce.ui.AppViewModel
import cl.uc.cimuce.ui.screens.contacto.ContactoScreen
import cl.uc.cimuce.ui.screens.detalle.DetalleScreen
import cl.uc.cimuce.ui.screens.explorar.ExplorarScreen
import cl.uc.cimuce.ui.screens.inicio.InicioScreen
import cl.uc.cimuce.ui.screens.novedades.NovedadesScreen
import cl.uc.cimuce.ui.screens.onboarding.OnboardingScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.ONBOARDING,
        modifier = modifier
    ) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(
                onComenzar = { perfil ->
                    appViewModel.elegirPerfil(perfil)
                    navController.navigate(Rutas.PRINCIPAL) {
                        popUpTo(Rutas.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        // Todo lo que muestra la barra inferior; su inicio (Inicio) es la base de la pila.
        navigation(startDestination = Rutas.INICIO, route = Rutas.PRINCIPAL) {
            composable(Rutas.INICIO) {
                val perfil by appViewModel.perfil.collectAsStateWithLifecycle()
                InicioScreen(
                    perfil = perfil,
                    onBuscar = { navController.navegarConVolver(Rutas.explorar(q = it)) },
                    onAbrirTipo = { navController.navegarConVolver(Rutas.explorar(tipo = it.name)) },
                    onAbrirDetalle = { navController.navigate(Rutas.detalle(it)) },
                    onVerNovedades = { navController.irAPestana(Pestana.NOVEDADES, Pestana.INICIO) },
                    onEscanearFeria = appViewModel::guardarDesdeFeria
                )
            }
            composable(
                route = Rutas.EXPLORAR,
                arguments = listOf(
                    navArgument(Rutas.ARG_TIPO) { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument(Rutas.ARG_Q) { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { entry ->
                // tipo y q llegan al ExplorarViewModel por el SavedStateHandle de la entrada.
                ExplorarScreen(
                    mostrarVolver = entry.savedStateHandle.get<Boolean>(Rutas.CON_VOLVER) == true,
                    onVolver = { navController.popBackStack() },
                    onAbrirDetalle = { navController.navigate(Rutas.detalle(it)) }
                )
            }
            composable(Rutas.NOVEDADES) { NovedadesScreen() }
            composable(Rutas.CONTACTO) {
                val perfil by appViewModel.perfil.collectAsStateWithLifecycle()
                val favoritos by appViewModel.favoritos.collectAsStateWithLifecycle()
                ContactoScreen(
                    perfilOnboarding = perfil,
                    favoritos = favoritos,
                    onQuitarFavorito = appViewModel::toggleFavorito,
                    onAbrirDetalle = { navController.navigate(Rutas.detalle(it)) }
                )
            }
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument(Rutas.ARG_ID_PROYECTO) { type = NavType.IntType })
            ) {
                // idProyecto llega al DetalleViewModel por el SavedStateHandle de la entrada.
                val favoritos by appViewModel.favoritos.collectAsStateWithLifecycle()
                DetalleScreen(
                    favoritos = favoritos,
                    onToggleFavorito = appViewModel::toggleFavorito,
                    onVolver = { navController.popBackStack() },
                    onAbrirIniciativa = { nuevoId ->
                        // Reemplaza el detalle actual: la profundidad no pasa de Inicio > Explorar > Detalle
                        // y "volver" regresa a la pantalla de origen, no al detalle anterior (NF2).
                        navController.navigate(Rutas.detalle(nuevoId)) {
                            popUpTo(Rutas.DETALLE) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Navega desde el contenido de una pantalla (no desde la barra) y marca la nueva entrada
 * para que muestre la flecha ←. Desde la pestaña, Inicio también queda debajo en la pila,
 * por eso "hay pantalla anterior" no basta para decidirlo.
 */
fun NavController.navegarConVolver(ruta: String) {
    navigate(ruta)
    currentBackStackEntry?.savedStateHandle?.set(Rutas.CON_VOLVER, true)
}

/**
 * Cambia de pestaña conservando el estado de cada una.
 * Si se toca la pestaña ya activa (p. ej. estando en un detalle), vuelve a su pantalla raíz.
 */
fun NavController.irAPestana(pestana: Pestana, pestanaActiva: Pestana?) {
    if (pestana == pestanaActiva) {
        popBackStack(pestana.ruta, inclusive = false)
        return
    }
    val destino = if (pestana == Pestana.EXPLORAR) Rutas.explorar() else pestana.ruta
    val inicioPrincipal = (graph.findNode(Rutas.PRINCIPAL) as NavGraph).findStartDestination()
    navigate(destino) {
        popUpTo(inicioPrincipal.id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
    // restoreState puede traer de vuelta un detalle que quedó abierto en esa pestaña;
    // la pestaña debe mostrar siempre su propia pantalla (con su estado guardado).
    popBackStack(pestana.ruta, inclusive = false)
}
