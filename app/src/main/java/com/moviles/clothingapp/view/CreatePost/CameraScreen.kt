package com.moviles.clothingapp.view.CreatePost

import android.Manifest
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



/*  CameraScreen View
*   This is where the user comes to when clicking on the bottom navigation icon (camera)
*   Requests for user permission to access the camera.
*
 */
@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    /* Request Camera Permission */
    val cameraPermissionGranted = remember { mutableStateOf(false) }
    RequestCameraPermission { granted ->
        cameraPermissionGranted.value = granted
    }

    if (!cameraPermissionGranted.value) return

    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA
                    val capture = ImageCapture.Builder().build()

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview, capture
                        )
                        imageCapture.value = capture
                    } catch (exc: Exception) {
                        exc.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )


        /* Go Back Button */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=60.dp, start = 20.dp), // padding of the go back
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
            }
        }

        /* Capture Button */
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp) // Size of the button
                    .background(Color.White, shape = CircleShape)
                    .border(4.dp, Color.Gray, CircleShape)
                    .clickable {
                        val capture = imageCapture.value ?: return@clickable
                        val outputFile =
                            File(context.externalCacheDir, "${System.currentTimeMillis()}.jpg")
                        val outputOptions =
                            ImageCapture.OutputFileOptions.Builder(outputFile).build()

                        capture.takePicture(
                            outputOptions,
                            cameraExecutor,
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    capturedImageUri = Uri.fromFile(outputFile)
                                    val encodedUri = URLEncoder.encode(
                                        capturedImageUri.toString(),
                                        StandardCharsets.UTF_8.toString()
                                    )

                                    Handler(Looper.getMainLooper()).post {
                                        navController.navigate("createPost/$encodedUri")
                                    }
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.printStackTrace()
                                }
                            }
                        )
                    }
            )
        }

    }
}


/*  Function to request Camera Permission and return the result using a callback
*   Accesses the permissions file of androidManifest.xml
 */
@Composable
fun RequestCameraPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
        onPermissionResult(isGranted)
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}
