package com.moviles.clothingapp.view.DetailedPost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.view.components.ConnectionBanner
import com.moviles.clothingapp.viewmodel.CartViewModel
import com.moviles.clothingapp.viewmodel.FavoritesViewModel
import com.moviles.clothingapp.viewmodel.PostViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailedPostScreen(
    navController: NavController,
    productId: Int,
    viewModel: PostViewModel = viewModel(),
    onBack: () -> Unit,
    isConnected: Boolean
) {
    val product by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val favoritesViewModel: FavoritesViewModel = viewModel()
    val favoriteIds by favoritesViewModel.favoriteIds.observeAsState(emptySet())

    val cartViewModel: CartViewModel = viewModel()
    val cartIds by cartViewModel.cartIds.observeAsState(emptySet())

    val isInCart = cartIds.contains(productId.toString())
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        viewModel.fetchPostById(productId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            product != null -> {
                val bucketId = "67ddf3860035ee6bd725"
                val projectId = "moviles"
                val imageUrl = if (product!!.image.startsWith("http")) {
                    product!!.image
                } else {
                    "https://cloud.appwrite.io/v1/storage/buckets/$bucketId/files/${product!!.image}/view?project=$projectId"
                }

                val isFavorite = favoriteIds.contains(productId.toString())

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    ConnectionBanner(isConnected = isConnected)
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = product!!.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Brand: ${product!!.brand}", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        text = "Price: $${product!!.price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                    Text(text = "Category: ${product!!.category}")
                    Text(text = "Group: ${product!!.group}")
                    Text(text = "Size: ${product!!.size}")
                    Text(text = "Color: ${product!!.color}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA0522D))
                        ) {
                            Text(text = "Back", color = Color.White)
                        }

                        Button(
                            onClick = {
                                cartViewModel.toggleCart(productId.toString())
                                coroutineScope.launch {
                                    val message = if (isInCart)
                                        "Producto removido del carrito"
                                    else
                                        "Producto agregado al carrito"
                                    snackbarHostState.showSnackbar(message)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInCart) Color.DarkGray else Color(0xFF2E7D32)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isInCart) "Remove from Cart" else "Add to Cart",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            favoritesViewModel.toggleFavorite(productId.toString())
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }

                        Text(
                            text = if (isFavorite) "Este producto está en tus favoritos" else "No está en favoritos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error loading product", color = Color.Red)
                }
            }
        }
    }
}
