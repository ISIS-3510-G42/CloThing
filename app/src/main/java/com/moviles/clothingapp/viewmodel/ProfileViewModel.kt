package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.moviles.clothingapp.model.UserRepository
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.model.UserData
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository()

    private val _user = MutableLiveData<UserData?>()
    val user: LiveData<UserData?> = _user

    private val _postedIds = MutableLiveData<Set<Int>>(emptySet())
    val postedIds: LiveData<Set<Int>> = _postedIds

    private val _postedProducts = MutableLiveData<List<PostData>>(emptyList())
    val postedProducts: LiveData<List<PostData>> = _postedProducts

    private val _boughtIds = MutableLiveData<Set<Int>>(emptySet())
    val boughtIds: LiveData<Set<Int>> = _boughtIds

    private val _boughtProducts = MutableLiveData<List<PostData>>(emptyList())
    val boughtProducts: LiveData<List<PostData>> = _boughtProducts

    // Load user data from email and update IDs
    fun loadUserData(userEmail: String) {
        viewModelScope.launch {
            val fetchedUser = repo.fetchUserByEmail(userEmail)
            _user.value = fetchedUser
            fetchedUser?.let {
                _postedIds.value = it.postedProducts.toSet()
                _boughtIds.value = it.boughtProducts.toSet()
            }
        }
    }

    // Filter all products by posted IDs
    fun updatePostedProducts(allProducts: List<PostData>) {
        val postedIds = _postedIds.value ?: emptySet()
        val products = allProducts.filter { post ->
            post.id?.toIntOrNull() in postedIds
        }
        _postedProducts.value = products
    }

    // Filter all products by bought IDs
    fun updateBoughtProducts(allProducts: List<PostData>) {
        val boughtIds = _boughtIds.value ?: emptySet()
        val products = allProducts.filter { post ->
            post.id?.toIntOrNull() in boughtIds
        }
        _boughtProducts.value = products
    }

    // Load current user's information
    fun loadCurrentUserData() {
        viewModelScope.launch {
            val email = repo.getCurrentUserEmail()
            if (email != null) {
                val fetchedUser = repo.fetchUserByEmail(email)
                _user.value = fetchedUser
                fetchedUser?.let {
                    _postedIds.value = it.postedProducts.toSet()
                    _boughtIds.value = it.boughtProducts.toSet()
                }
            }
        }
    }
}