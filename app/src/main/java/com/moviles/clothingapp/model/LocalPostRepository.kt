package com.moviles.clothingapp.model

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

class LocalPostRepository(application: Context) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "posts_db"
    ).build()

    private val postDao = db.postDao()

    val allPosts: Flow<List<PostEntity>> = postDao.getAllPosts()

    suspend fun insertPost(post: PostEntity) {
        postDao.insertPost(post)
    }

    suspend fun getAllPostList(): List<PostEntity>{
        return postDao.getAllPostsList()
    }

}