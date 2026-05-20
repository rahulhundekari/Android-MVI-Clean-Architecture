package com.demo.android_mvi_clean_architecture.ui.navigatiobar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.demo.android_mvi_clean_architecture.navigation.Screen

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationItem> = listOf(
        BottomNavigationItem.Feed,
        BottomNavigationItem.MyFavorites
    )
)

sealed class BottomNavigationItem(
    val tabName: String,
    val imageVector: ImageVector,
    val screen: Screen
) {
    data object Feed :
        BottomNavigationItem("Feed", imageVector = Icons.Default.DynamicFeed, Screen.Feed)

    data object MyFavorites : BottomNavigationItem(
        "My Favorites", imageVector = Icons.Default.FavoriteBorder,
        Screen.Favorites
    )
}