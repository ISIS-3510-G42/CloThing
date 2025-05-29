package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.moviles.clothingapp.model.UserRepository
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository()

    private val _user = MutableLiveData<UserData?>()
    val user: LiveData<UserData?> = _user

    private val _boughtIds = MutableLiveData<Set<Int>>(emptySet())
    val boughtIds: LiveData<Set<Int>> = _boughtIds

    private val _boughtProducts = MutableLiveData<List<PostData>>(emptyList())
    val boughtProducts: LiveData<List<PostData>> = _boughtProducts

    // Filter all products by bought IDs (moved to a background thread)
    fun updateBoughtProducts(allProducts: List<PostData>) {
        viewModelScope.launch(Dispatchers.Default) { // Run this task on the Default dispatcher (CPU-bound task)
            val boughtIds = _boughtIds.value ?: emptySet()
            val products = allProducts.filter { post ->
                post.id?.toIntOrNull() in boughtIds
            }
            withContext(Dispatchers.Main) { // Switch back to Main dispatcher to update UI
                _boughtProducts.value = products
            }
        }
    }

    // Load current user data (network call or data fetching from Firebase)
    fun loadCurrentUserData() {
        viewModelScope.launch(Dispatchers.IO) { // Use IO dispatcher for network/database calls
            val email = repo.getCurrentUserEmail()
            if (email != null) {
                val fetchedUser = repo.fetchUserByEmail(email)
                withContext(Dispatchers.Main) { // Switch back to Main thread to update UI
                    _user.value = fetchedUser
                }
                fetchedUser?.let { user ->
                    // Parse boughtProducts (move parsing to background thread)
                    val boughtIdsList = parseJsonArrayString(user.boughtProducts)
                    withContext(Dispatchers.Main) { // Update LiveData on the main thread
                        _boughtIds.value = boughtIdsList.toSet()
                    }
                }
            }
        }
    }

    // Helper: Convert "[1,2,3]" → List<Int>
    private fun parseJsonArrayString(jsonString: String): List<Int> {
        return try {
            JSONArray(jsonString)
                .let { array ->
                    (0 until array.length()).map { array.getInt(it) }
                }
        } catch (e: Exception) {
            emptyList() // Fallback if parsing fails
        }
    }
}
