package com.moviles.clothingapp.view.HomeView


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moviles.clothingapp.R
import com.moviles.clothingapp.model.Category
import com.moviles.clothingapp.ui.theme.dmSansFamily
import com.moviles.clothingapp.ui.theme.figtreeFamily


@Composable
fun CategorySection(categoryList: List<Category>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Categorías",
                fontSize = 20.sp,
                fontFamily = dmSansFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 5.dp)
            )
            IconButton(
                onClick = { /* TODO: Ir a ver todas las categorias */ },
                modifier = Modifier
                    .size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See All"
                )
            }
        }

        // Lista de categorias
        LazyRow {
            items(categoryList) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Card(
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.size(80.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = category.name, fontSize = 14.sp,
                        fontFamily = figtreeFamily, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}


val categoryList = listOf(
    Category("Camisetas", R.drawable.tshirt),
    Category("Chaquetas", R.drawable.jacket),
    Category("Tenis", R.drawable.sneakers),
    Category("Pantalones", R.drawable.pants),
    Category("Sweaters", R.drawable.sweater),


)
