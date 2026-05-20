package com.demo.android_mvi_clean_architecture.util.preview

import androidx.compose.runtime.Composable
import com.demo.android_mvi_clean_architecture.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}