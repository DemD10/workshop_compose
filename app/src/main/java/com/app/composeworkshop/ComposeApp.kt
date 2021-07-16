package com.app.composeworkshop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.app.composeworkshop.Destinations.EFFECTS
import com.app.composeworkshop.Destinations.PLAYER
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object Destinations {
    const val PLAYER = "player"
    const val EFFECTS = "effects"
}

@Composable
fun ComposeApp() {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()

    AppTheme {
        NavHost(navController = navController, startDestination = PLAYER) {
            composable(PLAYER) {
                val viewModel: PlayerViewModel = viewModel(factory = PlayerViewModelFactory(navController))
                PlayerScreen(viewModel)
            }
            composable(
                "$EFFECTS/{videoProgress}?duration={duration}",
                arguments = listOf(
                    navArgument("videoProgress") { type = NavType.LongType },
                    navArgument("duration") { type = NavType.LongType }
                )
            ) {
                EffectsScreen(
                    videoProgress = it.arguments?.getLong("videoProgress") ?: 0,
                    duration = it.arguments?.getLong("duration") ?: 0
                )
            }
        }
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }
    }
}