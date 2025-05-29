package com.moviles.clothingapp.view.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.view.Discover.PostItem
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.viewmodel.CartViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import com.moviles.clothingapp.view.components.ConnectionBanner

@Composable
fun CartScreen(
    navController: NavHostController,
    isConnected: Boolean,
    cartViewModel: CartViewModel,
    allProducts: List<PostData>
) {
    val cartProducts by cartViewModel.cartProducts.observeAsState(emptyList())

    LaunchedEffect(allProducts, cartViewModel.cartIds.observeAsState().value) {
        cartViewModel.updateCartProducts(allProducts)
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
                    text = "Cart",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* Acción opcional */ }) {
                    Icon(Icons.Rounded.ShoppingCart, contentDescription = "Cart Icon")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    cartViewModel.buyProducts { success ->
                        if (success) {
                            // Mostrar un mensaje, navegar, etc.
                            println("Compra completada exitosamente")
                        } else {
                            println("Error al realizar la compra")
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = "Buy"
                    )
                },
                text = { Text("¡Comprar!") },
                containerColor = Color(0xFFFFD54F), // Amarillo/marrón suave
                contentColor = Color.Black // Texto e ícono en negro para contraste
            )
        }



    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ConnectionBanner(isConnected = isConnected)

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = cartProducts) { post ->
                    PostItem(post) {
                        navController.navigate("detailedPost/${post.id}")
                    }
                }
            }
        }
    }
}
