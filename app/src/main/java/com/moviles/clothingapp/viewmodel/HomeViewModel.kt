package com.moviles.clothingapp.viewmodel

import androidx.lifecycle.*
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.repository.PostRepository
import kotlinx.coroutines.launch



/*  HomeViewModel:
*   - Fetches the information from the post repository (the one that connects with the API),
*     to send the information of the products to the Categories and FeaturedProducts views.
*/
class HomeViewModel : ViewModel() {
    data class ProductUI(
        val image: String,
        val name: String,
        val brand: String,
        val price: String,
        val color: String,
        val size: String,
        val group: String // Genero: hombre o mujer.
    )

    private val postRepository = PostRepository()


    private val _postData = MutableLiveData<List<PostData>>()
    val postData: LiveData<List<ProductUI>> = _postData.map { posts ->
        posts.map { product ->
            ProductUI(
                image = product.image,
                name = product.name,
                brand = product.brand,
                price = "$${product.price}",
                color = product.color,
                size = product.size,
                group = product.group


            )
        }
    }

    init {
        getPostData()
    }

    fun getPostData() {
        viewModelScope.launch {
            val postResult = postRepository.fetchRepository()
            _postData.postValue(postResult ?: emptyList())
        }
    }
}
