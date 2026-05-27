package com.demo.android_mvi_clean_architecture.ui.main

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.demo.android_mvi_clean_architecture.navigation.Graph
import com.demo.android_mvi_clean_architecture.navigation.Screen
import com.demo.android_mvi_clean_architecture.ui.navigatiobar.NavigationBarNestedGraph
import com.demo.android_mvi_clean_architecture.ui.navigatiobar.NavigationBarScreen
import com.demo.android_mvi_clean_architecture.ui.search.SearchScreen
import com.demo.android_mvi_clean_architecture.ui.search.SearchView
import com.demo.android_mvi_clean_architecture.ui.search.SearchViewModel
import com.demo.android_mvi_clean_architecture.util.composableHorizontalSlide
import com.demo.android_mvi_clean_architecture.util.sharedViewModel

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {

    NavHost(
        navController = mainNavController,
        startDestination = Screen.NavigationBar,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Screen.NavigationBar> { backStack ->
            val nestedNavController = rememberNavController()

            NavigationBarScreen(
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController),
                mainRouter = MainRouter(mainNavController),
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                nestedNavController = nestedNavController
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graph.Main::class
                )
            }
        }

        composableHorizontalSlide<Screen.Search> {
            val viewModel = hiltViewModel<SearchViewModel>()

            SearchScreen(
                mainNavHostController = mainNavController,
                viewModel = viewModel
            )
        }
    }
}