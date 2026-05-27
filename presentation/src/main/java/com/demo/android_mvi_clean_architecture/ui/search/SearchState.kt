package com.demo.android_mvi_clean_architecture.ui.search

data class SearchUiState(
    val showDefaultState: Boolean = true,
    val showLoading: Boolean = false,
    val showNoMoviesFound: Boolean = true,
    val errorMessage: String? = null
)

sealed class SearchNavigationState {
    data class MovieDetails(val movieId: Int) : SearchNavigationState()
}