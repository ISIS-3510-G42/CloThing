package com.moviles.clothingapp.view.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.view.Discover.PostItem
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.viewmodel.FavoritesViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel,
    allProducts: List<PostData>
) {
    // Hacer que la lista de productos favoritos sea reactiva
    val favoriteProducts by favoritesViewModel.favoriteProducts.observeAsState(emptyList())

    LaunchedEffect(allProducts, favoritesViewModel.favoriteIds.observeAsState().value) {
        favoritesViewModel.updateFavoriteProducts(allProducts)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favoritos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* Acción opcional */ }) {
                    Icon(Icons.Rounded.Favorite, contentDescription = "Favorite Icon")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items = favoriteProducts) { post ->
                PostItem(post) {
                    navController.navigate("detailedPost/${post.id}")
                }
            }
        }
    }
}
