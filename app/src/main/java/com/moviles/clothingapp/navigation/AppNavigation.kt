package com.moviles.clothingapp.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moviles.clothingapp.view.CreatePost.CameraScreen
import com.moviles.clothingapp.view.CreatePost.CreatePostScreen
import com.moviles.clothingapp.view.DetailedPost.DetailedPostScreen
import com.moviles.clothingapp.view.Discover.DiscoverScreen
import com.moviles.clothingapp.view.HomeView.MainScreen
import com.moviles.clothingapp.view.Login.CreateAccountScreen
import com.moviles.clothingapp.view.Login.LoginScreen
import com.moviles.clothingapp.view.Map.MapScreen
import com.moviles.clothingapp.viewmodel.FavoritesViewModel
import com.moviles.clothingapp.view.PostsCreated.PostsCreatedScreen
import com.moviles.clothingapp.view.Profile.ProfileScreen
import com.moviles.clothingapp.viewmodel.HomeViewModel
import com.moviles.clothingapp.viewmodel.LoginViewModel
import com.moviles.clothingapp.viewmodel.PostViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moviles.clothingapp.view.favorites.FavoritesScreen
import androidx.compose.foundation.layout.Box
import com.moviles.clothingapp.viewmodel.ProfileViewModel


/* Navigation component called to change between pages
*   - each page is a composable, here we define the routes to call, and which component it invokes.
*   - all the other pages must be added below the home screen composable.
*   - Each composable here declares a route which must be the same stated in the component.
* */
@Composable
fun AppNavigation(navController: NavHostController,
                  isConnected: Boolean,
                  loginViewModel: LoginViewModel
) {

    //Check connection to internet
    if (!isConnected) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Red)) {
            Text(
                text = "No hay conexión a internet",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    /* Start navigation in login page. Route: login */
    NavHost(navController = navController, startDestination = "login") {

        //Login view composable
        composable("login") {
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        //Create account view composable
        composable("createAccount") {
            CreateAccountScreen(loginViewModel, navController)
        }

        //Home view composable
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel()
            MainScreen(navController, homeViewModel, isConnected)

        }

        //Discover view composable
        composable("discover/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val postViewModel: PostViewModel = viewModel()
            DiscoverScreen(navController, postViewModel, query, isConnected)
        }

        //Detailed post composable... shows the details of a post
        composable(
            route = "detailedPost/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val postViewModel: PostViewModel = viewModel()
            DetailedPostScreen(
                navController = navController,
                productId = postId,
                viewModel = postViewModel,
                onBack = { navController.popBackStack() },
                onAddToCart = { /* lógica para agregar al carrito */ },
                isConnected = isConnected
            )
        }

        //Camera view composable
        composable("camera") {
            CameraScreen(navController)
        }

        //Create a post view composable
        composable("createPost/{encodedUri}") { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("encodedUri") ?: ""
            val decodedUri = Uri.decode(encodedUri)
            CreatePostScreen(navController, decodedUri)
        }

        //Locally created posts composable
        composable("postCreated") {
            PostsCreatedScreen(navController = navController)
        }

        //Map composable
        composable("map/") {
            MapScreen(navController, isConnected = isConnected)
        }

        //Favorites view composable
        composable("favorites") {
            val favoritesViewModel: FavoritesViewModel = viewModel()
            val postViewModel: PostViewModel = viewModel()
            val allProducts by postViewModel.posts.collectAsState()

            FavoritesScreen(navController,isConnected, favoritesViewModel, allProducts)
        }

        //Profile view composable
        composable("profile"){
            val profileViewModel: ProfileViewModel = viewModel()
            val postViewModel: PostViewModel = viewModel()
            val allProducts by postViewModel.posts.collectAsState()
            ProfileScreen(navController, isConnected, profileViewModel, loginViewModel, allProducts)
        }

    }
}
