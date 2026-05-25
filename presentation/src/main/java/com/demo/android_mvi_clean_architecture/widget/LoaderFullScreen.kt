package com.demo.android_mvi_clean_architecture.widget

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun LoaderFullScreen(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Loader()
    }
}

@Composable
fun Loader() {
    CircularProgressIndicator(color = Color.Green)
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoaderFullScreenPreview() {
    Surface {
        Loader()
    }
}