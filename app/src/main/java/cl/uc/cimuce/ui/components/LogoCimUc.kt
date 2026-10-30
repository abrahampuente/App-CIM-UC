package cl.uc.cimuce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uc.cimuce.ui.theme.CIMUCETheme
import cl.uc.cimuce.ui.theme.Caramelo
import cl.uc.cimuce.ui.theme.SansCuerpo
import cl.uc.cimuce.ui.theme.SerifTitulos

/** Logo del header (Inicio y onboarding): cuadrado caramelo con casa y nombre del centro. */
@Composable
fun LogoCimUc(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(48.dp).background(Caramelo, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = "CENTRO UC DE INNOVACIÓN EN",
                fontFamily = SansCuerpo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "Madera · CIM UC",
                fontFamily = SerifTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E3F2A)
@Composable
private fun LogoCimUcPreview() {
    CIMUCETheme { LogoCimUc(Modifier.padding(16.dp)) }
}
