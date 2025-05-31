package com.moviles.clothingapp.view.HomeView

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.moviles.clothingapp.R
import com.moviles.clothingapp.viewmodel.HomeViewModel

@Composable
fun MainScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    isConnected: Boolean
) {
    val searchText = remember { mutableStateOf("") }

    val trace: Trace = remember { FirebasePerformance.getInstance().newTrace("MainScreen_Loading") }
    LaunchedEffect(Unit) {
        trace.start()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Banner conexion a internet
            item {
                if (!isConnected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(androidx.compose.ui.graphics.Color.Red)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No internet connection, showing cache",
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(80.dp)
                )
            }
            item {
                SearchBar(
                    searchText = searchText.value,
                    onSearchTextChange = { newText -> searchText.value = newText },
                    onSearchSubmit = {
                        navController.navigate("discover/${searchText.value}")
                    }
                )
            }
            item { CategorySection(categoryList = categoryList) }
            item { FeaturedProducts(homeViewModel) }
        }
    }

    LaunchedEffect(Unit) {
        trace.stop()
    }
}
