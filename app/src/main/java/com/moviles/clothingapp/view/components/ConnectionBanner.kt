package com.moviles.clothingapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box


@Composable
fun ConnectionBanner(isConnected: Boolean) {
    if (!isConnected) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.Red)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Internet Connection, Showing Cache",
                color = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}
