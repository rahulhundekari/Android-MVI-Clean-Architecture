package com.demo.android_mvi_clean_architecture.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.demo.android_mvi_clean_architecture.di.AppSettingsSharedPreference
import com.demo.android_mvi_clean_architecture.navigation.Graph
import com.demo.android_mvi_clean_architecture.ui.theme.AppTheme
import com.demo.android_mvi_clean_architecture.widget.NoNetworkConnectionBanner
import com.demo.domain.util.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val DARK_MODE = "dark_mode"
    }

    @Inject
    @AppSettingsSharedPreference
    lateinit var appSettings: SharedPreferences

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private fun isDarkModeEnabled(): Boolean = appSettings.getBoolean(DARK_MODE, false)

    private fun enableDarkMode(enable: Boolean) =
        appSettings.edit().putBoolean(DARK_MODE, enable).apply()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            var darkMode by remember { mutableStateOf(isDarkModeEnabled()) }

            AppTheme(darkMode) {
                Column {
                    val networkState by networkMonitor.networkState.collectAsState(null)

                    networkState?.let {
                        if (it.isOnline.not()) {
                            NoNetworkConnectionBanner()
                        }
                    }

                    MainGraph(
                        darkMode = darkMode,
                        mainNavController = navController,
                        onThemeUpdated = {
                            val updated = !darkMode
                            enableDarkMode(updated)
                            darkMode = updated
                        }
                    )

                }
            }
        }
    }
}