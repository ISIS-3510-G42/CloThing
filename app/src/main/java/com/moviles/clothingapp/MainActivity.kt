package com.moviles.clothingapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.moviles.clothingapp.navigation.AppNavigation
import com.moviles.clothingapp.viewmodel.LoginViewModel
import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.perf.FirebasePerformance
import com.moviles.clothingapp.utils.ConnectivityObserver
import androidx.compose.runtime.getValue
import com.moviles.clothingapp.model.UserRepository


class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var userRepository: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver = ConnectivityObserver(applicationContext)
        auth = Firebase.auth
        userRepository = UserRepository()
        loginViewModel = LoginViewModel(auth, userRepository)
        Log.d("FirebasePerf", "Firebase Performance Monitoring initialized: ${FirebasePerformance.getInstance()}")
        firebaseAnalytics = Firebase.analytics

        val deviceInfo = Bundle().apply {
            putString("device_model", Build.MODEL)
            putString("device_brand", Build.BRAND)
            putString("os_version", Build.VERSION.RELEASE)
        }

        firebaseAnalytics.logEvent("device_info", deviceInfo)
        Log.d("DEVICES", ""+deviceInfo)

        setContent {
            val navController = rememberNavController()
            val isConnected by connectivityObserver.isConnected.collectAsState(initial = false)
            AppNavigation(
                navController = navController,
                isConnected = isConnected,
                loginViewModel = loginViewModel
            )
        }
    }
}