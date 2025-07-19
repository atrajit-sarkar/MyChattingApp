package com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel

@Composable
fun RetrieveChatAppTheme(
    viewModel: ChatAppViewModel,
) {
    val savedSettings = viewModel.savedSettings.observeAsState()
    Log.d("saved_settings", "ChatAppTheme: ${savedSettings.value}")


    // Switch Case to update the theme............
    when (savedSettings.value?.chatTheme) {
        ChatScreenThemeNames.Default.name -> {
            viewModel.changeThemeName(theme = DefaultTheme)
        }

        ChatScreenThemeNames.DeshiRussian.name -> {
            viewModel.changeThemeName(theme = DeshiRussianTheme)

        }

        ChatScreenThemeNames.LalKhuji.name -> {
            viewModel.changeThemeName(theme = LalKhujiTheme)
        }

        ChatScreenThemeNames.NilJyoni.name -> {
            viewModel.changeThemeName(theme = NilJyoniTheme)
        }

        ChatScreenThemeNames.Custom.name -> {
            val customChatTheme =
                viewModel.getCustomChatThemeById(
                    savedSettings.value?.customChatThemeId?.toInt() ?: 0
                )
                    .observeAsState()
            viewModel.changeOwnMessageColor(
                Color(
                    customChatTheme.value?.ownMessageColor?.toLong() ?: 1
                )
            )
            viewModel.changeNotOwnMessageColor(
                Color(
                    customChatTheme.value?.notOwnMessageColor?.toLong() ?: 1
                )
            )
            viewModel.changeOwnBorderColor(
                Color(
                    customChatTheme.value?.ownBorderColor?.toLong() ?: 1
                )
            )
            viewModel.changeNotOwnBorderColor(
                Color(
                    customChatTheme.value?.notOwnBorderColor?.toLong() ?: 1
                )
            )
            viewModel.changeLockOpenColor(
                Color(
                    customChatTheme.value?.lockOpenColor?.toLong() ?: 1
                )
            )
            viewModel.changeViewOnceColor(
                Color(
                    customChatTheme.value?.viewOnceColor?.toLong() ?: 1
                )
            )
            viewModel.changeOpenedColor(Color(customChatTheme.value?.openedColor?.toLong() ?: 1))
            viewModel.changeLockColor(Color(customChatTheme.value?.lockColor?.toLong() ?: 1))
            viewModel.changeThemeName(theme = CustomTheme(viewModel))
        }


    }


}