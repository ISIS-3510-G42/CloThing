package com.moviles.clothingapp.view.DetailedPost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.moviles.clothingapp.viewmodel.FavoritesViewModel
import com.moviles.clothingapp.viewmodel.PostViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun DetailedPostScreen(
    productId: Int,
    viewModel: PostViewModel = viewModel(),
    onBack: () -> Unit,
    onAddToCart: () -> Unit
) {
    val product by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favoritesViewModel: FavoritesViewModel = viewModel()

    // 👇 Observar lista de favoritos como estado
    val favoriteIds by favoritesViewModel.favoriteIds.observeAsState(emptySet())

    LaunchedEffect(productId) {
        viewModel.fetchPostById(productId)
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

            // 👇 Saber si este producto está en favoritos
            val isFavorite = favoriteIds.contains(productId.toString())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
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
                Text(text = "Price: $${product!!.price}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Green)
                Text(text = "Category: ${product!!.category}")
                Text(text = "Group: ${product!!.group}")
                Text(text = "Size: ${product!!.size}")
                Text(text = "Color: ${product!!.color}")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Boton de atras
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA0522D))
                    ) {
                        Text(text = "Back", color = Color.White)
                    }

                    //Boton de anadir al carrito
                    Button(
                        onClick = onAddToCart,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text(text = "Add to Cart", color = Color.White)
                    }

                    //Boton de favoritos
                    IconButton(onClick = {
                        favoritesViewModel.toggleFavorite(productId.toString())
                    }) {
                        Icon(
                            imageVector = if (favoriteIds.contains(productId.toString())) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favoriteIds.contains(productId.toString())) Color.Red else Color.Gray
                        )
                    }


                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error loading product", color = Color.Red)
            }
        }
    }
}
