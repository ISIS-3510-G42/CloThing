package com.moviles.clothingapp.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Stable
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val size: String,
    val category: String,
    val group: String,
    val price: String,
    val image: String,
    val color: String,
    )
