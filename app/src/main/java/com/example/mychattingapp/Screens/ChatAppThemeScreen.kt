package com.example.mychattingapp.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.ChatScreenThemeNames
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.DefaultTheme
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.DeshiRussianTheme
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.LalKhujiTheme
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.NilJyoniTheme
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.WallPaperNames
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.WallpaperTheme

@Composable
fun ChatThemeScreen(viewModel: ChatAppViewModel, navController: NavController) {
    val storedIndex = remember { mutableStateOf("") }
    val customThemes = viewModel.customChatTheme.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
//            .padding(bottom = 10.dp)
            .padding(top = 30.dp),
    ) {
        Text(
            text = "Chat theme",
            color = Color.White,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow for Themes - Row 1
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ThemeCard(
                    gradientColors = listOf<Color>(
                        DefaultTheme.ownMessageColor,
                        DefaultTheme.notOwnMessageColor,
                        DefaultTheme.ownBorderColor,
                        DefaultTheme.notOwnBorderColor
                    ),
                    isSelected = storedIndex.value == "1",
                    viewModel = viewModel,
                    themeName = ChatScreenThemeNames.Default.name,
                    index = "1",
                    storedIndex = storedIndex,
                )
            }
            item {
                ThemeCard(
                    gradientColors = listOf<Color>(
                        LalKhujiTheme.ownMessageColor,
                        LalKhujiTheme.notOwnMessageColor,
                        LalKhujiTheme.ownBorderColor,
                        LalKhujiTheme.notOwnBorderColor
                    ),
                    isSelected = storedIndex.value == "3",
                    viewModel = viewModel,
                    themeName = ChatScreenThemeNames.LalKhuji.name,
                    index = "3",
                    storedIndex = storedIndex
                )
            }
            item {
                ThemeCard(
                    gradientColors = listOf<Color>(
                        DeshiRussianTheme.ownMessageColor,
                        DeshiRussianTheme.notOwnMessageColor,
                        DeshiRussianTheme.ownBorderColor,
                        DeshiRussianTheme.notOwnBorderColor
                    ),
                    isSelected = storedIndex.value == "2",
                    viewModel = viewModel,
                    themeName = ChatScreenThemeNames.DeshiRussian.name,
                    index = "2",
                    storedIndex = storedIndex
                )
            }

            item {
                ThemeCard(
                    gradientColors = listOf<Color>(
                        NilJyoniTheme.ownMessageColor,
                        NilJyoniTheme.notOwnMessageColor,
                        NilJyoniTheme.ownBorderColor,
                        NilJyoniTheme.notOwnBorderColor
                    ),
                    isSelected = storedIndex.value == "4",
                    viewModel = viewModel,
                    themeName = ChatScreenThemeNames.NilJyoni.name,
                    index = "4",
                    storedIndex = storedIndex
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // LazyRow for Themes - Row 2
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {

                WallpaperCard(
                    viewModel = viewModel,
                    wallPaperName = WallPaperNames.Mountains.name,
                    isSelected = storedIndex.value == "5",
                    index = "5",
                    storedIndex = storedIndex,
                    id = WallpaperTheme.Mountains
                )
            }
            item {

                WallpaperCard(
                    viewModel = viewModel,
                    wallPaperName = WallPaperNames.Ocean.name,
                    isSelected = storedIndex.value == "6",
                    index = "6",
                    storedIndex = storedIndex,
                    id = WallpaperTheme.Ocean
                )
            }
            item {

                WallpaperCard(
                    viewModel = viewModel,
                    wallPaperName = WallPaperNames.Sunset.name,
                    isSelected = storedIndex.value == "7",
                    index = "7",
                    storedIndex = storedIndex,
                    id = WallpaperTheme.Sunset
                )
            }
            item {

                WallpaperCard(
                    viewModel = viewModel,
                    wallPaperName = WallPaperNames.Beach.name,
                    isSelected = storedIndex.value == "8",
                    index = "8",
                    storedIndex = storedIndex,
                    id = WallpaperTheme.Beach
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(customThemes.value?.toList() ?: emptyList()) { theme ->
                CustomThemeCard(
                    viewModel = viewModel,
                    gradientColors = listOf(
                        Color(theme.ownMessageColor.toLong()),
                        Color(theme.notOwnMessageColor.toLong()),
                        Color(theme.ownBorderColor.toLong()),
                        Color(theme.notOwnBorderColor.toLong()),

                        ),
                    themeName = theme.themeName,
                    isSelected = storedIndex.value == "${9 + theme.id}",
                    index = "${9 + theme.id}",
                    storedIndex = storedIndex,
                    customChatTheme = theme

                )
            }
            item {
                CreateThemeCard(
                    viewModel = viewModel,
                    themeName = "Create Custom Theme",
                    navController = navController


                )

            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Customise",
            color = Color.White,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Customise options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomOption(icon = Icons.Default.CheckCircle, label = "Chat colour")
            CustomOption(icon = Icons.Default.Warning, label = "Wallpaper")
        }
    }
}

@Composable
fun ThemeCard(
    viewModel: ChatAppViewModel,
    themeName: String,
    gradientColors: List<Color>, // Use gradient colors
    isSelected: Boolean,
    index: String = "",
    storedIndex: MutableState<String>,
) {
    val savedSettings = viewModel.savedSettings.observeAsState()

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp)) // More pronounced rounded corners
            .background(
                brush = Brush.linearGradient(gradientColors), // Gradient background
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = {
                    storedIndex.value = index
                    savedSettings.value?.let {
                        viewModel.updateSettings(
                            it.copy(
                                chatTheme = themeName,
                                wallpaper = ""
                            )
                        )
                    }
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), // Increased padding for better layout
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = themeName,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp, // Adjust font size
                    fontWeight = FontWeight.Bold, // Bold for emphasis
                    fontFamily = FontFamily.SansSerif, // Modern font family
                    shadow = Shadow( // Add a subtle shadow
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun CreateThemeCard(
    viewModel: ChatAppViewModel,
    themeName: String,
    isSelected: Boolean = true,
    navController: NavController
) {
    val savedSettings = viewModel.savedSettings.observeAsState()

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp)) // More pronounced rounded corners
//            .background(
//                brush = Brush.linearGradient(gradientColors), // Gradient background
//                shape = RoundedCornerShape(12.dp)
//            )
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = {
                    navigateIfNotFast() {
                        navController.navigate("custom_theme_create_screen")
                    }
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), // Increased padding for better layout
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = themeName,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp, // Adjust font size
                    fontWeight = FontWeight.Bold, // Bold for emphasis
                    fontFamily = FontFamily.SansSerif, // Modern font family
                    shadow = Shadow( // Add a subtle shadow
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomThemeCard(
    viewModel: ChatAppViewModel,
    themeName: String,
    gradientColors: List<Color>, // Use gradient colors
    isSelected: Boolean,
    index: String = "",
    storedIndex: MutableState<String>,
    customChatTheme: CustomChatTheme
) {
    val savedSettings = viewModel.savedSettings.observeAsState()

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp)) // More pronounced rounded corners
            .background(
                brush = Brush.linearGradient(gradientColors), // Gradient background
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .combinedClickable(
                onLongClick = {
                    viewModel.deleteCustomChatTheme(customChatTheme)
                },
                onClick = {
                    storedIndex.value = index
                    savedSettings.value?.let {
                        viewModel.updateSettings(
                            it.copy(
                                chatTheme = ChatScreenThemeNames.Custom.name,
                                customChatThemeId = customChatTheme.id
                            )
                        )
                        viewModel.changeOwnMessageColor(Color(customChatTheme.ownMessageColor.toLong()))
                        viewModel.changeNotOwnMessageColor(Color(customChatTheme.notOwnMessageColor.toLong()))
                        viewModel.changeOwnBorderColor(Color(customChatTheme.ownBorderColor.toLong()))
                        viewModel.changeNotOwnBorderColor(Color(customChatTheme.notOwnBorderColor.toLong()))
                        viewModel.changeLockOpenColor(Color(customChatTheme.lockOpenColor.toLong()))
                        viewModel.changeViewOnceColor(Color(customChatTheme.viewOnceColor.toLong()))
                        viewModel.changeOpenedColor(Color(customChatTheme.openedColor.toLong()))
                        viewModel.changeLockColor(Color(customChatTheme.lockColor.toLong()))

                    }
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), // Increased padding for better layout
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = themeName,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp, // Adjust font size
                    fontWeight = FontWeight.Bold, // Bold for emphasis
                    fontFamily = FontFamily.SansSerif, // Modern font family
                    shadow = Shadow( // Add a subtle shadow
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun WallpaperCard(
    viewModel: ChatAppViewModel,
    wallPaperName: String,
    isSelected: Boolean,
    index: String = "",
    storedIndex: MutableState<String>,
    id: Int

) {
    val savedSettings = viewModel.savedSettings.observeAsState()

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp)) // More pronounced rounded corners
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = {
                    storedIndex.value = index
                    savedSettings.value?.let {
                        viewModel.updateSettings(
                            it.copy(
                                wallpaper = wallPaperName,
                            )
                        )
                    }
                }
            )
    ) {
        Image(
            painter = painterResource(id = id), // Replace with your image resource
            contentDescription = null, // Decorative image
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop, // Ensures the image covers the entire screen
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), // Increased padding for better layout
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = wallPaperName,
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp, // Adjust font size
                    fontWeight = FontWeight.Bold, // Bold for emphasis
                    fontFamily = FontFamily.SansSerif, // Modern font family
                    shadow = Shadow( // Add a subtle shadow
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}


@Composable
fun CustomOption(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Green,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.White, style = MaterialTheme.typography.body2)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ChatThemeScreenPreview() {
//    ChatThemeScreen(
//        viewModel = ChatAppViewModel(
//
//
//        )
//    )
//}
