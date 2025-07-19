package com.example.mychattingapp.NavHost

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.Screens.ChatAppProfileSection
import com.example.mychattingapp.Screens.ChatScreen
import com.example.mychattingapp.Screens.ChatThemeScreen
import com.example.mychattingapp.Screens.Contacts
import com.example.mychattingapp.Screens.CustomThemeCreateScreen
import com.example.mychattingapp.Screens.DevicesScreenUI
import com.example.mychattingapp.Screens.HomeScreen
import com.example.mychattingapp.Screens.LoginSignupScreen
import com.example.mychattingapp.Screens.SettingsScreen
import com.example.mychattingapp.Screens.SetupTimeInstructionsScreen
import com.example.mychattingapp.Screens.SwipeableScreensWithTabs
import com.google.firebase.auth.FirebaseAuth

const val durationMillis = 320 // Slightly increased duration for smoother transitions

@Composable
fun Navigation(viewModel: ChatAppViewModel) {
    val navController = rememberNavController()
    var startDestination: String = ""
    startDestination = if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
        "login_screen"
    } else {
        "home_screen"
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {
//        composable("home_screen") { HomeScreen(navController = navController, viewModel = viewModel) }
        composable("home_screen") {
            SwipeableScreensWithTabs(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("chat_screen/{chatId}") {
            it.arguments?.getString("chatId")
                ?.let { it1 ->
                    ChatScreen(
                        navController = navController,
                        uid = it1,
                        viewModel = viewModel
                    )
                }
        }

        composable(
            route = "chat_screen/{chatId}/{messageId}",
            arguments = listOf(
                navArgument("chatId") { type = NavType.IntType },
                navArgument("messageId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.toString() ?: ""
            val messageId = backStackEntry.arguments?.getInt("messageId") ?: 0

            ChatScreen(
                uid = chatId,
                viewModel = viewModel,
                navController = navController,
                messageId = messageId
            )
        }
        composable("settings_screen") {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
        composable("allcontact_screen") {
            Contacts(navController = navController, viewModel = viewModel)
        }
        composable("login_screen") {
            LoginSignupScreen(navController = navController, viewModel = viewModel)
        }
        composable("profile_screen") {
            ChatAppProfileSection(navController = navController)
        }
        composable("setup_time_screen") {
            SetupTimeInstructionsScreen()
        }
        composable("devices_screen") {
            DevicesScreenUI()
        }
        composable("chat_theme_screen") {
            ChatThemeScreen(viewModel = viewModel, navController = navController)

        }
        composable("custom_theme_create_screen") {
            CustomThemeCreateScreen(viewModel = viewModel, navController = navController)
        }

    }
}
