package com.demo.android_mvi_clean_architecture.ui.base


data class FeedUiState(
    val showLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class FeedNavigationState {
    data class MovieDetails(val movieId: Int) : FeedNavigationState()
}