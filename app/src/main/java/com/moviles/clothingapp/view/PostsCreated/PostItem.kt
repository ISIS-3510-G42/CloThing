package com.moviles.clothingapp.view.PostsCreated

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.moviles.clothingapp.model.PostEntity

@Composable
fun PostItem(post: PostEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (post.image.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(post.image),
                    contentDescription = "Imagen del post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = post.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Marca: ${post.brand}")
            Text(text = "Talla: ${post.size}")
            Text(text = "Color: ${post.color}")
            Text(text = "Categoría: ${post.category}")
            Text(text = "Grupo: ${post.group}")
            Text(text = "Precio: $${post.price}")
        }
    }
}