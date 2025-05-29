package com.moviles.clothingapp.viewmodel

import android.app.Application
import android.os.Environment
import android.util.Log
import android.widget.Toast // ✅ UPDATED
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.CartRepository
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.model.ReceiptRepository
import com.moviles.clothingapp.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CartRepository.CartRepository(application)
    private val userRepo = UserRepository()
    private val receiptRepo = ReceiptRepository(application)

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
            Log.d("CartViewModel", " Iniciando proceso de compra...")

            val userEmail = userRepo.getCurrentUserEmail()
            Log.d("CartViewModel", " Email actual: $userEmail")

            if (userEmail != null) {
                val user = userRepo.fetchUserByEmail(userEmail)
                Log.d("CartViewModel", " Usuario obtenido: $user")

                if (user != null) {
                    val currentBought = parseStringList(user.boughtProducts)
                    val cartList = _cartIds.value?.toList() ?: emptyList()

                    Log.d("CartViewModel", " Productos ya comprados: $currentBought")
                    Log.d("CartViewModel", " Productos en carrito: $cartList")

                    val newBought = currentBought + cartList
                    val updatedString = newBought.toSet().joinToString(prefix = "[", postfix = "]")

                    Log.d("CartViewModel", " Nueva cadena para boughtProducts: $updatedString")

                    val updatedUser = userRepo.updateUserProducts(userEmail, null, updatedString)
                    Log.d("CartViewModel", " Resultado de updateUserProducts: $updatedUser")

                    if (updatedUser != null) {
                        val cartItems = _cartProducts.value ?: emptyList()
                        val receiptText = generateReceiptText(cartItems)
                        val receiptResult = receiptRepo.saveReceiptToFile(receiptText)

                        receiptResult.onSuccess { path ->
                            Log.d("CartViewModel", " Receipt saved at: $path")

                            val receiptDir = receiptRepo.getExternalFilesDir() //  Get receipt dir
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    getApplication(),
                                    "Receipt saved in: $receiptDir",
                                    Toast.LENGTH_LONG
                                ).show() //  Show Toast
                            }
                        }.onFailure { e ->
                            Log.e("CartViewModel", " Failed to save receipt", e)
                        }

                        repo.clearCart()
                        Log.d("CartViewModel", " Carrito limpiado correctamente")

                        withContext(Dispatchers.Main) {
                            _cartIds.value = emptySet()
                            _cartProducts.value = emptyList()
                            onResult(true)
                        }
                        return@launch
                    } else {
                        Log.e("CartViewModel", " Falló la actualización del usuario")
                    }
                } else {
                    Log.e("CartViewModel", " No se encontró el usuario")
                }
            } else {
                Log.e("CartViewModel", " userEmail es null")
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

    private fun generateReceiptText(products: List<PostData>): String {
        val builder = StringBuilder()
        val email = userRepo.getCurrentUserEmail()
        val name = email?.substringBefore("@") ?: "Unknown User"

        builder.appendLine("Clothing App Receipt for $name")
        builder.appendLine("Date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
        builder.appendLine("===================================")

        var total = 0.0
        for (product in products) {
            val priceString = product.price.replace(",", "")
            val price = priceString.toDoubleOrNull() ?: 0.0

            Log.d("CartViewModel", "Product: ${product.name}, Price: ${product.price}, Type: ${product.price::class.java.simpleName}, Parsed Price: $price")

            if (price == 0.0) {
                Log.w("CartViewModel", "⚠️ Product price is invalid or zero: ${product.name}")
            }

            builder.appendLine("${product.name} - ${product.brand} - ${product.size} - $${"%.2f".format(price)}")
            total += price
        }

        builder.appendLine("===================================")
        builder.appendLine("Total: $${"%.2f".format(total)}")
        return builder.toString()
    }
}
