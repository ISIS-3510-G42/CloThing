package com.moviles.clothingapp.view.HomeView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moviles.clothingapp.R
import com.moviles.clothingapp.ui.theme.dmSansFamily
import com.moviles.clothingapp.viewmodel.WeatherViewModel
import androidx.compose.foundation.clickable


/* Dynamic Promo banner (image and text) depending on user's weather */
@Composable
fun PromoBanner(bannerType: WeatherViewModel.BannerType?, navController: NavController) {
    val imageRes = when (bannerType) {
        WeatherViewModel.BannerType.LIGHT_CLOTHING -> R.drawable.light_clothing_banner
        WeatherViewModel.BannerType.WARM_CLOTHING -> R.drawable.warm_clothing_banner
        WeatherViewModel.BannerType.RAINY_WEATHER -> R.drawable.rainy_weather_banner
        WeatherViewModel.BannerType.CLOUDY_WEATHER -> R.drawable.cloudy_weather_banner
        WeatherViewModel.BannerType.NO_WEATHER_DATA, null -> R.drawable.promo_image
    }

    /* Custom banner messages */
    val bannerText = when (bannerType) {
        WeatherViewModel.BannerType.LIGHT_CLOTHING -> "Ropa ligera para el calor!"
        WeatherViewModel.BannerType.WARM_CLOTHING -> "Mantente abrigado con estos productos"
        WeatherViewModel.BannerType.RAINY_WEATHER -> "Especial para días lluviosos"
        WeatherViewModel.BannerType.CLOUDY_WEATHER -> "Las Mejores Prendas para Días nublados"
        WeatherViewModel.BannerType.NO_WEATHER_DATA, null -> "Mejores precios"
    }

    /* Updated categories for navigation: We use this variables (e.g. Frio) to retrieve clothes with that category from API. */
    val categoryId = when (bannerType) {
        WeatherViewModel.BannerType.LIGHT_CLOTHING -> "Calor"
        WeatherViewModel.BannerType.WARM_CLOTHING -> "Frio"
        WeatherViewModel.BannerType.RAINY_WEATHER -> "Lluvia"
        WeatherViewModel.BannerType.CLOUDY_WEATHER -> "Nublado"
        WeatherViewModel.BannerType.NO_WEATHER_DATA, null -> "Oferta"
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp)
            .clickable {
                navController.navigate("category/$categoryId")
            },
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))

        )
        Text(
            text = bannerText,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = dmSansFamily,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 8f
                )
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)

        )
    }

}
