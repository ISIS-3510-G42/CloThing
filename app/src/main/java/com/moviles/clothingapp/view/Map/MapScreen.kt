package com.moviles.clothingapp.view.Map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.viewmodel.MapLogicViewModel

@Composable
fun MapScreen(navController: NavController, viewModel: MapLogicViewModel = viewModel()) {
    val userLocation by viewModel.userLocation.observeAsState()
    val shopLocations by viewModel.shopLocations.observeAsState(emptyList())


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Tiendas de ropa usada",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp) /* Height for the map */
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = viewModel.cameraPositionState
                ) {
                    /* User location marker (blue marker) */
                    userLocation?.let { latLng ->
                        Marker(
                            state = rememberMarkerState(position = latLng),
                            title = "You are here",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        )
                    }

                    /* Shop Markers */
                    shopLocations.forEach { shop ->
                        Marker(
                            state = rememberMarkerState(position = shop.location),
                            title = shop.name
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Near You:",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )


            /* List of nearby shops */
            LazyColumn {
                items(shopLocations) { shop ->
                    ShopItem(shop)
                }
            }
        }
    }
}



/* Composable for each item of the shops */
@Composable
fun ShopItem(shop: MapLogicViewModel.Shop) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = "Shop Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = shop.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = shop.address,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal)
        }
    }
}


