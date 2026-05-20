package com.demo.android_mvi_clean_architecture.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


fun <T> singleSharedFlow() = MutableSharedFlow<T>(
    replay = 0,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

@Composable
fun <T> Flow<T>.CollectAsEffect(effect: suspend (T) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        onEach(effect).launchIn(this)
    }
}