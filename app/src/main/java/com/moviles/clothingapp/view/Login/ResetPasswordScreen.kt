package com.moviles.clothingapp.view.Login

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.moviles.clothingapp.viewmodel.ResetPasswordViewModel

@Composable
fun ResetPasswordScreen(resetPasswordViewModel: ResetPasswordViewModel = viewModel(), navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val resetPasswordResult by resetPasswordViewModel.resetPasswordResult.observeAsState()
    val trace: Trace = remember { FirebasePerformance.getInstance().newTrace("ResetPassword_Loading") }

    // Efecto popup de link enviado
    LaunchedEffect(resetPasswordResult) {
        trace.start()
        when (resetPasswordResult) {
            is ResetPasswordViewModel.ResetPasswordResult.Success -> {
                Toast.makeText(context, "Correo de recuperacion enviado.", Toast.LENGTH_SHORT).show()
            }
            is ResetPasswordViewModel.ResetPasswordResult.Failure -> {
                Toast.makeText(context, "Error: ${(resetPasswordResult as ResetPasswordViewModel.ResetPasswordResult.Failure).errorMessage}", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // ...
            }
        }
        trace.stop()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )

    {
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Volver")
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { resetPasswordViewModel.sendPasswordResetEmail(email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restablecer contraseña")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Si una cuenta con ese correo existe, se enviara un correo de recuperacion.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

