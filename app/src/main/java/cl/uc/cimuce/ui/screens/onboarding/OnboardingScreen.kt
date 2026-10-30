package cl.uc.cimuce.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.ui.components.LogoCimUc
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos
import cl.uc.cimuce.ui.theme.VerdeClaro
import cl.uc.cimuce.ui.theme.VerdeOscuro

private val PerfilUsuario.icono: ImageVector
    get() = when (this) {
        PerfilUsuario.EMPRESA -> Icons.Filled.Business
        PerfilUsuario.ACADEMIA -> Icons.Filled.School
        PerfilUsuario.SECTOR_PUBLICO -> Icons.Filled.AccountBalance
    }

private val PerfilUsuario.descripcion: String
    get() = when (this) {
        PerfilUsuario.EMPRESA -> "Soluciones y transferencia tecnológica para tu negocio"
        PerfilUsuario.ACADEMIA -> "Colaboración en investigación y formación"
        PerfilUsuario.SECTOR_PUBLICO -> "Normativa, vivienda y políticas públicas"
    }

/** Onboarding "Matchmaking": el usuario elige su perfil para personalizar recomendaciones. */
@Composable
fun OnboardingScreen(onComenzar: (PerfilUsuario) -> Unit) {
    var seleccionado by rememberSaveable { mutableStateOf<PerfilUsuario?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(VerdeOscuro, VerdeClaro)))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
        ) {
            LogoCimUc()
            Spacer(Modifier.height(40.dp))
            Text(
                text = "¿Cuál es tu perfil?",
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                color = Color.White
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Personalizaremos tus recomendaciones",
                fontFamily = SansCuerpo,
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PerfilUsuario.entries.forEach { perfil ->
                    TarjetaPerfil(
                        perfil = perfil,
                        seleccionado = perfil == seleccionado,
                        onClick = { seleccionado = perfil }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        Button(
            onClick = { seleccionado?.let(onComenzar) },
            enabled = seleccionado != null,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Caramelo,
                contentColor = Color.White,
                disabledContainerColor = Caramelo.copy(alpha = 0.35f),
                disabledContentColor = Color.White.copy(alpha = 0.6f)
            )
        ) {
            Text("Comenzar", fontFamily = SansCuerpo, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun TarjetaPerfil(perfil: PerfilUsuario, seleccionado: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White.copy(alpha = if (seleccionado) 0.20f else 0.12f))
            .border(2.dp, if (seleccionado) Caramelo else Color.Transparent, forma)
            .selectable(selected = seleccionado, role = Role.RadioButton, onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(perfil.icono, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = perfil.etiqueta,
                fontFamily = SansCuerpo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = Color.White
            )
            Text(
                text = perfil.descripcion,
                fontFamily = SansCuerpo,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun OnboardingPreview() {
    CIMUCETheme { OnboardingScreen(onComenzar = {}) }
}
