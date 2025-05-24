package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.FavoritesRepository
import com.moviles.clothingapp.model.PostData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = FavoritesRepository.FavoritesRepository(application)

    private val _favoriteIds = MutableLiveData<Set<String>>(repo.getFavoriteIds())
    val favoriteIds: LiveData<Set<String>> = _favoriteIds

    private val _favoriteProducts = MutableLiveData<List<PostData>>(emptyList())
    val favoriteProducts: LiveData<List<PostData>> = _favoriteProducts

    fun updateFavoriteProducts(allProducts: List<PostData>) {
        viewModelScope.launch(Dispatchers.IO) {
            val filtered = allProducts.filter { isFavorite(it.id ?: "") }
            withContext(Dispatchers.Main) {
                _favoriteProducts.value = filtered
            }
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.toggleFavorite(productId)
            val updatedIds = repo.getFavoriteIds()
            withContext(Dispatchers.Main) {
                _favoriteIds.value = updatedIds
            }
        }
    }

    fun isFavorite(productId: String): Boolean {
        return repo.isFavorite(productId)
    }
}
