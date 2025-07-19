package com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.R

@Composable
fun RetrieveWallPaper(viewModel: ChatAppViewModel) {
    val savedSettings = viewModel.savedSettings.observeAsState()
    val themeName by viewModel.themeName.collectAsState()
    when (savedSettings.value?.wallpaper) {
        WallPaperNames.Mountains.name -> {
            viewModel.changeWallPaperId(WallpaperTheme.Mountains)
        }

        WallPaperNames.Ocean.name -> {
            viewModel.changeWallPaperId(WallpaperTheme.Ocean)

        }

        WallPaperNames.Beach.name -> {
            viewModel.changeWallPaperId(WallpaperTheme.Beach)
        }

        WallPaperNames.Sunset.name -> {
            viewModel.changeWallPaperId(WallpaperTheme.Sunset)
        }

        else -> {
            viewModel.changeWallPaperId(themeName.chatAppBackGroudImage)
        }
    }
}