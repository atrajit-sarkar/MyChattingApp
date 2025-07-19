import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import java.io.File
import java.util.concurrent.Executors
import androidx.lifecycle.LifecycleOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
//import androidx.compose.material.icons.filled.Camera
//import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val showCamera = remember { mutableStateOf(false) }
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    if (showCamera.value) {
        CameraCaptureScreen(
            onImageCaptured = { uri ->
                showCamera.value = false
                capturedImageUri.value = uri
            },
            onClose = {
                showCamera.value = false
            }
        )
    } else {
        ChatUI(
            onCameraClick = {
                showCamera.value = true
            },
            capturedImage = capturedImageUri.value
        )
    }
}

@Composable
fun ChatUI(onCameraClick: () -> Unit, capturedImage: Uri?) {
    Column {
        // Chat messages go here (omitted for brevity)

        capturedImage?.let { uri ->
            // Display the captured image in the chat
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Captured Image",
                modifier = Modifier.size(150.dp)
            )
        }

        // Bottom bar with camera icon
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCameraClick) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Open Camera"
                )
            }
            // Add other UI elements for text input and send button
        }
    }
}

@Composable
fun CameraCaptureScreen(
    onImageCaptured: (Uri) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
//    val imageCapture = remember { ImageCapture.Builder().build() }
    val outputDirectory = remember { context.cacheDir }
    val photoUri = remember { mutableStateOf<Uri?>(null) }

    Box(Modifier.fillMaxSize()) {
//        val previewView = remember { PreviewView(context) }
//        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Initialize Camera
//        LaunchedEffect(Unit) {
//            val cameraProvider = cameraProviderFuture.get()
//            val preview = Preview.Builder().build().apply {
//                setSurfaceProvider(previewView.surfaceProvider)
//            }
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner,
//                cameraSelector,
//                preview,
//                imageCapture
//            )
//        }

        // Shutter button
        IconButton(
            onClick = {
//                val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
//                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//                imageCapture.takePicture(
//                    outputOptions,
//                    cameraExecutor,
//                    object : ImageCapture.OnImageSavedCallback {
//                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                            val uri = Uri.fromFile(photoFile)
//                            photoUri.value = uri
//                            onImageCaptured(uri)
//                        }
//
//                        override fun onError(exception: ImageCaptureException) {
//                            // Handle errors
//                        }
//                    }
//                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Capture Image",
                tint = Color.White
            )
        }

        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Camera",
                tint = Color.White
            )
        }
    }
}

