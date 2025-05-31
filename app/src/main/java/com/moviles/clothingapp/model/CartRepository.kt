package com.moviles.clothingapp.model

import android.content.Context

class CartRepository {
    class CartRepository(context: Context) {
        private val prefs = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        private val CART_KEY = "cart"

        fun getCartIds(): Set<String> {
            return prefs.getStringSet(CART_KEY, emptySet()) ?: emptySet()
        }

        fun isCart(productId: String): Boolean {
            return getCartIds().contains(productId)
        }

        fun toggleCart(productId: String) {
            val current = getCartIds().toMutableSet()
            if (current.contains(productId)) {
                current.remove(productId)
            } else {
                current.add(productId)
            }

            prefs.edit().putStringSet(CART_KEY, current).apply()
        }

        // ✅ Borra todos los productos del carrito
        fun clearCart() {
            prefs.edit().remove(CART_KEY).apply()
        }
    }
}
