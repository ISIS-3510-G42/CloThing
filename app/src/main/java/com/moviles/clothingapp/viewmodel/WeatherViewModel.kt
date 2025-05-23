package com.moviles.clothingapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.WeatherResponse
import com.moviles.clothingapp.repository.WeatherRepository
import kotlinx.coroutines.launch

/* View Model for Weather:
*   - Connects directly with weatherData and weatherRepository (which get the user's location and weather info)
*   - The extracted information from Model is processed to base a recommendation for the PromoBanner in MainScreen page.
*   - Depending on temperature or raining, the PromoBanner changes to recommend different categories.
*   - Since LoginScreen is loaded this process begins as the query is to an external API. (based on user location)
 */
class WeatherViewModel(context: Context) : ViewModel() {
    private val weatherRepository = WeatherRepository(context)
    val weatherData = MutableLiveData<WeatherResponse?>()
    val bannerType = MutableLiveData<BannerType>()

    enum class BannerType {
        LIGHT_CLOTHING,
        WARM_CLOTHING,
        RAINY_WEATHER,
        CLOUDY_WEATHER,
        NO_WEATHER_DATA
    }

    fun fetchWeatherData() {
        viewModelScope.launch {
            val data = weatherRepository.getWeatherData()
            weatherData.value = data
            generateRecommendations(data)
        }
    }

    private fun generateRecommendations(data: WeatherResponse?) {
        if (data != null) {
            val temperature = data.main.temp
            val description = data.weather.firstOrNull()?.description ?: ""
            val banner = when {
                temperature > 20 -> BannerType.LIGHT_CLOTHING
                temperature < 10 -> BannerType.WARM_CLOTHING
                description.contains("rain", ignoreCase = true) -> BannerType.RAINY_WEATHER
                else -> BannerType.CLOUDY_WEATHER
            }
            Log.d("WeatherViewModel", "Banner value: $banner")
            bannerType.setValue(banner)
        } else {
            bannerType.setValue(BannerType.NO_WEATHER_DATA)
        }
    }
}