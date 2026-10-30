package cl.uc.cimuce

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import cl.uc.cimuce.ui.AppViewModel
import cl.uc.cimuce.ui.CimUcApp
import cl.uc.cimuce.ui.theme.CIMUCETheme

class MainActivity : ComponentActivity() {

    // Compartido a nivel de Activity: perfil y favoritos sobreviven a la navegación.
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La app es siempre clara: íconos oscuros por defecto, aunque el sistema esté en modo oscuro.
        // CimUcApp los cambia a claros en las pantallas con header verde o imagen bajo la status bar.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            CIMUCETheme {
                CimUcApp(appViewModel)
            }
        }
    }
}
