package com.moviles.clothingapp.view.HomeView


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.moviles.clothingapp.ui.theme.figtreeFamily
import com.moviles.clothingapp.viewmodel.HomeViewModel
import coil.compose.rememberAsyncImagePainter


/* SECCION DESTACADOS */
@Composable
fun FeaturedProducts(viewModel: HomeViewModel) {
    val products by viewModel.postData.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Destacados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 5.dp)
            )
        }

        // Wrap in a Box with height to avoid infinite scrolling error
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // Set finite height
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}




@Composable
fun ProductCard(product: HomeViewModel.ProductUI){
    val bucketId = "67ddf3860035ee6bd725"
    val projectId = "moviles"
    val imageUrl = if (product.image.startsWith("http")) {
        product.image // Fallback if it's already a URL or local resource
    } else {
        "https://cloud.appwrite.io/v1/storage/buckets/$bucketId/files/${product.image}/view?project=$projectId"
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
            model = imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.brand,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = product.name,
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = product.price,
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
