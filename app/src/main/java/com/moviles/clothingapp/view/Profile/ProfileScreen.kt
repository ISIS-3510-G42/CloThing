package com.moviles.clothingapp.view.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.view.Discover.PostItem
import com.moviles.clothingapp.view.HomeView.BottomNavigationBar
import com.moviles.clothingapp.view.components.ConnectionBanner
import com.moviles.clothingapp.viewmodel.LoginViewModel
import com.moviles.clothingapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    isConnected: Boolean,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    allProducts: List<PostData>
) {
    val user by profileViewModel.user.observeAsState()
    val postedProducts by profileViewModel.postedProducts.observeAsState(emptyList())
    val boughtProducts by profileViewModel.boughtProducts.observeAsState(emptyList())

    // Update lists when IDs change
    LaunchedEffect(profileViewModel.postedIds.value) {
        profileViewModel.updatePostedProducts(allProducts)
    }
    LaunchedEffect(profileViewModel.boughtIds.value) {
        profileViewModel.updateBoughtProducts(allProducts)
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUserData()
    }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* Acción opcional */ }) {
                    Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile Icon")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ConnectionBanner(isConnected = isConnected)

            Spacer(modifier = Modifier.height(16.dp))

            // User Information
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = user?.name ?: "Nombre no disponible",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user?.email ?: "Correo no disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Posted Products
            Text(
                text = "Productos publicados",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(postedProducts) { post ->
                    PostItem(post) {
                        navController.navigate("detailedPost/${post.id}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bought Products
            Text(
                text = "Productos comprados",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(boughtProducts) { post ->
                    PostItem(post) {
                        navController.navigate("detailedPost/${post.id}")
                    }
                }
            }

            //Log out button
            Button(
                onClick = {
                    loginViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true } // Limpia la pila para que no vuelva al Home con back
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión")
            }

        }
    }
}
