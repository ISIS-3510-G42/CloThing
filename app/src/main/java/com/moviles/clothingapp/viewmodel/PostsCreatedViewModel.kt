package com.moviles.clothingapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.LocalPostRepository
import com.moviles.clothingapp.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsCreatedViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepo = LocalPostRepository(application.applicationContext)

    private val _allPosts = mutableStateOf<List<PostEntity>>(emptyList())
    val allPosts: State<List<PostEntity>> get() = _allPosts

    init{
        fetchPost()
    }

    private fun fetchPost(){
        viewModelScope.launch{
            val posts = withContext(Dispatchers.IO){
                localRepo.getAllPostList()
            }
            _allPosts.value = posts
        }
    }
}
