package com.moviles.clothingapp.view.HomeView


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moviles.clothingapp.ui.theme.dmSansFamily


@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical=5.dp),

        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionItem(Icons.Outlined.FavoriteBorder, "Favoritos")
        QuickActionItem(Icons.Outlined.History, "Historial")
        QuickActionItem(Icons.Outlined.Person, "Seguidos")
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, text: String) {
    Button(
        onClick = { /* TODO: Ir a estas paginas.. o cambiar la interfaz */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            //.width(120.dp)
            .padding(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text, tint = Color.Black)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text, color = Color.Black, fontSize = 11.sp,
                fontFamily = dmSansFamily, fontWeight = FontWeight.Medium)
        }
    }
}
