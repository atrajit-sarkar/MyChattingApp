package com.example.mychattingapp.Media.Image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel


@Composable
fun ChatImage(
    imageUrl: String = "",
    localUri: String,
    modifier: Modifier = Modifier,
    viewModel: ChatAppViewModel,

    ) {
    val uploadingProfilePhoto = viewModel.uploadingProfilePhoto.collectAsState()
    Box(
        modifier = modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .size(200.dp) // Adjust size as needed
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = if (uploadingProfilePhoto.value && localUri != "") localUri else "https://raw.githubusercontent.com/gongobongofounder/MCA_ImageRepo/main/${imageUrl}.jpg"
            ),
            contentDescription = "WhatsApp DP",
            modifier = Modifier
                .fillMaxSize()
                // Add a green border
                .clickable(
                    onClick = {

                    }
                ),
            contentScale = ContentScale.Crop // Automatically crop to fit the circular shape
        )

    }
}
