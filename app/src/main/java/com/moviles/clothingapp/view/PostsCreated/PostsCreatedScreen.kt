package com.moviles.clothingapp.view.PostsCreated

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.moviles.clothingapp.viewmodel.PostsCreatedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsCreatedScreen(navController: NavController,viewModel: PostsCreatedViewModel = viewModel()) {
    val postList by viewModel.allPosts

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos Creados") })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(postList) { post ->
                PostItem(post = post)
            }
        }
    }
}
