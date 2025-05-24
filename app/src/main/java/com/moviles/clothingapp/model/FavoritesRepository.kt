package com.moviles.clothingapp.model

import android.content.Context

class FavoritesRepository {
    class FavoritesRepository(context: Context) {
        private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

        fun getFavoriteIds(): Set<String> {
            return prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        }

        fun isFavorite(productId: String): Boolean {
            return getFavoriteIds().contains(productId)
        }


        fun toggleFavorite(productId: String) {
            val current = getFavoriteIds().toMutableSet()
            if (current.contains(productId)) {
                current.remove(productId)
            } else {
                current.add(productId)
            }

            prefs.edit().putStringSet("favorites", current).apply()
        }
    }
}