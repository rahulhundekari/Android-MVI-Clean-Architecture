package com.demo.android_mvi_clean_architecture.ui.main

import androidx.navigation.NavHostController
import com.demo.android_mvi_clean_architecture.navigation.Screen

class MainRouter(
    private val navHostController: NavHostController
) {

    fun navigateToSearch() {
        navHostController.navigate(Screen.Search)
    }

    fun navigateToMovieDetails(movieId: Int) {
        navHostController.navigate(Screen.MovieDetails(movieId))
    }
}