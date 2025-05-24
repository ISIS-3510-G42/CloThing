package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.LocalPostRepository
import com.moviles.clothingapp.model.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PostsCreatedViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepo = LocalPostRepository(application.applicationContext)

    val allPosts: Flow<List<PostEntity>> = localRepo.allPosts
}
