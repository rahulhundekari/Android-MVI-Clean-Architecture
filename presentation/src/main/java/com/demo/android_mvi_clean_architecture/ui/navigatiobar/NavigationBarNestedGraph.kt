package com.demo.android_mvi_clean_architecture.ui.navigatiobar

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.demo.android_mvi_clean_architecture.navigation.Screen
import com.demo.android_mvi_clean_architecture.ui.base.FeedScreen
import com.demo.android_mvi_clean_architecture.ui.base.FeedViewModel
import com.demo.android_mvi_clean_architecture.ui.main.MainRouter
import com.demo.android_mvi_clean_architecture.util.composableHorizontalSlide
import com.demo.android_mvi_clean_architecture.util.sharedViewModel
import kotlin.reflect.KClass

@Composable
fun NavigationBarNestedGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    parentRoute: KClass<*>?
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed,
        route = parentRoute
    ) {
        composableHorizontalSlide<Screen.Feed> { backStack ->
            val viewModel = hiltViewModel<FeedViewModel>()
            FeedScreen(
                mainRouter = MainRouter(mainNavController),
                feedViewModel = viewModel,
                navigationBarSharedViewModel = backStack.sharedViewModel(navController = mainNavController)
            )
        }
    }
}