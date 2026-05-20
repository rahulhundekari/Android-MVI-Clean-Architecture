package com.demo.android_mvi_clean_architecture.ui.navigatiobar

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.demo.android_mvi_clean_architecture.navigation.route
import com.demo.android_mvi_clean_architecture.ui.main.MainRouter
import com.demo.android_mvi_clean_architecture.ui.theme.colors
import com.demo.android_mvi_clean_architecture.util.preview.PreviewContainer
import com.demo.android_mvi_clean_architecture.widget.BottomNavigationBar
import com.demo.android_mvi_clean_architecture.widget.TopBar

@Composable
fun NavigationBarScreen(
    sharedViewModel: NavigationBarSharedViewModel,
    mainRouter: MainRouter,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit,
    nestedNavController: NavHostController,
    content: @Composable () -> Unit
) {

    val uiState = NavigationBarUiState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Movie Feed",
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                onSearchClick = {
                    mainRouter.navigateToSearch()
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                uiState.bottomItems,
                navController = nestedNavController,
                onItemClick = { bottomItem ->
                    val currentPageRoute = nestedNavController.currentDestination?.route
                    val clickedPageRoute = bottomItem.screen
                    val notSamePage = clickedPageRoute.route() != currentPageRoute
                    if (notSamePage) {
                        nestedNavController.navigate(clickedPageRoute) {
                            launchSingleTop = true
                            popUpTo(nestedNavController.graph.startDestinationId)
                        }
                    }
                    sharedViewModel.onBottomItemClicked(bottomItem)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationBarScreenPreview() = PreviewContainer {
    val navController = rememberNavController()
    val mainRouter = MainRouter(
        navController
    )
    NavigationBarScreen(
        sharedViewModel = NavigationBarSharedViewModel(),
        mainRouter = mainRouter,
        darkMode = isSystemInDarkTheme(),
        nestedNavController = navController,
        onThemeUpdated = {

        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(colors.background)
        ) {
            Text(
                text = "Screen Content",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )

        }
    }

}