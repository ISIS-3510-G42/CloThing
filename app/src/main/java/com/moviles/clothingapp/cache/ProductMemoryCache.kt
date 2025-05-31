package com.moviles.clothingapp.cache

import android.util.LruCache
import com.moviles.clothingapp.model.PostData

class ProductMemoryCache(maxSize: Int = 10) : LruCache<String, PostData>(maxSize) {

    fun putAll(products: List<PostData>) {
        products.take(maxSize()).forEach { product ->
            product.id?.let { put(it, product) }
        }
    }

    fun getAll(): List<PostData> = snapshot().values.toList()

    fun isNotEmpty(): Boolean = size() > 0
    fun isEmpty(): Boolean = size() == 0
}
