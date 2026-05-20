package com.demo.android_mvi_clean_architecture.widget

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.demo.android_mvi_clean_architecture.navigation.route
import com.demo.android_mvi_clean_architecture.ui.navigatiobar.BottomNavigationItem
import com.demo.android_mvi_clean_architecture.util.preview.PreviewContainer

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    navController: NavController,
    onItemClick: (BottomNavigationItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar {
        items.forEach { item ->
            val selected = item.screen.route() == backStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(imageVector = item.imageVector, contentDescription = null)
                },
                label = {
                    Text(text = item.tabName)
                }
            )

        }
    }
}

@Preview(name = "light")
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewBottomNavigationBar() {
    PreviewContainer {
        BottomNavigationBar(
            items = listOf(BottomNavigationItem.Feed, BottomNavigationItem.MyFavorites),
            rememberNavController()
        ){}
    }
}