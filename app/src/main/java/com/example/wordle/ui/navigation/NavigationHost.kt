package com.example.wordle.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordle.ui.screens.history.HistoryScreen
import com.example.wordle.ui.screens.home.HomeScreen
import com.example.wordle.ui.screens.spalsh.SplashScreen
import com.example.wordle.ui.screens.spalsh.SplashScreenViewModel

@Composable
fun Navigation(windowSizeClass: WindowSizeClass, contrast: Boolean = false) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Splash.route) {

        composable(Screens.Splash.route) {
            SplashScreen(navController, viewModel = hiltViewModel<SplashScreenViewModel>())
        }

        composable(Screens.Home.route) {
            HomeScreen(navController = navController, contrast = contrast,
                windowSizeClass = windowSizeClass)
        }

        composable(Screens.History.route) {
            HistoryScreen(navController)
        }
    }
}