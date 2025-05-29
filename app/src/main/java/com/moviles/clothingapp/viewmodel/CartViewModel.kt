package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.CartRepository
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log


class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CartRepository.CartRepository(application)
    private val userRepo = UserRepository()

    private val _cartIds = MutableLiveData<Set<String>>(repo.getCartIds())
    val cartIds: LiveData<Set<String>> = _cartIds

    private val _cartProducts = MutableLiveData<List<PostData>>(emptyList())
    val cartProducts: LiveData<List<PostData>> = _cartProducts

    fun updateCartProducts(allProducts: List<PostData>) {
        viewModelScope.launch(Dispatchers.IO) {
            val filtered = allProducts.filter { isCart(it.id ?: "") }
            withContext(Dispatchers.Main) {
                _cartProducts.value = filtered
            }
        }
    }

    fun toggleCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.toggleCart(productId)
            val updatedIds = repo.getCartIds()
            withContext(Dispatchers.Main) {
                _cartIds.value = updatedIds
            }
        }
    }

    fun isCart(productId: String): Boolean {
        return repo.isCart(productId)
    }

    fun buyProducts(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("CartViewModel", "🛒 Iniciando proceso de compra...")

            val userEmail = userRepo.getCurrentUserEmail()
            Log.d("CartViewModel", "📧 Email actual: $userEmail")

            if (userEmail != null) {
                val user = userRepo.fetchUserByEmail(userEmail)
                Log.d("CartViewModel", "👤 Usuario obtenido: $user")

                if (user != null) {
                    val currentBought = parseStringList(user.boughtProducts)
                    val cartList = _cartIds.value?.toList() ?: emptyList()

                    Log.d("CartViewModel", "🛍️ Productos ya comprados: $currentBought")
                    Log.d("CartViewModel", "🛒 Productos en carrito: $cartList")

                    val newBought = currentBought + cartList
                    val updatedString = newBought.toSet().joinToString(prefix = "[", postfix = "]")

                    Log.d("CartViewModel", "📦 Nueva cadena para boughtProducts: $updatedString")

                    val updatedUser = userRepo.updateUserProducts(userEmail, null, updatedString)
                    Log.d("CartViewModel", "✅ Resultado de updateUserProducts: $updatedUser")

                    if (updatedUser != null) {
                        repo.clearCart()
                        Log.d("CartViewModel", "🧹 Carrito limpiado correctamente")

                        withContext(Dispatchers.Main) {
                            _cartIds.value = emptySet()
                            _cartProducts.value = emptyList()
                            onResult(true)
                        }
                        return@launch
                    } else {
                        Log.e("CartViewModel", "❌ Falló la actualización del usuario")
                    }
                } else {
                    Log.e("CartViewModel", "❌ No se encontró el usuario")
                }
            } else {
                Log.e("CartViewModel", "❌ userEmail es null")
            }

            withContext(Dispatchers.Main) {
                onResult(false)
            }
        }
    }


    private fun parseStringList(input: String?): List<String> {
        return input?.removeSurrounding("[", "]")
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()
    }
}

