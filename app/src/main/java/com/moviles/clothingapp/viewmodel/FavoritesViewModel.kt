package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moviles.clothingapp.model.FavoritesRepository
import com.moviles.clothingapp.model.PostData

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = FavoritesRepository.FavoritesRepository(application)

    private val _favoriteIds = MutableLiveData<Set<String>>(repo.getFavoriteIds())
    val favoriteIds: LiveData<Set<String>> = _favoriteIds

    private val _favoriteProducts = MutableLiveData<List<PostData>>(emptyList())
    val favoriteProducts: LiveData<List<PostData>> = _favoriteProducts

    fun updateFavoriteProducts(allProducts: List<PostData>) {
        _favoriteProducts.value = allProducts.filter { isFavorite(it.id ?: "") }
    }

    fun toggleFavorite(productId: String) {
        repo.toggleFavorite(productId)
        _favoriteIds.value = repo.getFavoriteIds()
    }

    fun isFavorite(productId: String): Boolean {
        return repo.isFavorite(productId)
    }
}
