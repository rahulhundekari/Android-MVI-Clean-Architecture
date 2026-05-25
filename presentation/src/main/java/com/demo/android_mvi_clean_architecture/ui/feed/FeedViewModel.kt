package com.demo.android_mvi_clean_architecture.ui.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.demo.android_mvi_clean_architecture.ui.base.BaseViewModel
import com.demo.android_mvi_clean_architecture.entities.MovieListItem
import com.demo.android_mvi_clean_architecture.ui.feed.usecase.GetMoviesWithSeparators
import com.demo.android_mvi_clean_architecture.util.singleSharedFlow
import com.demo.domain.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    getMoviesWithSeparators: GetMoviesWithSeparators
) : BaseViewModel() {

    val movies: Flow<PagingData<MovieListItem>> = getMoviesWithSeparators.movies(
        pageSize = 90
    ).cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<FeedUiState> = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<FeedNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlow()
    val refreshListState = _refreshListState.asSharedFlow()

    init {
        observeNetworkState()
    }

    fun onMovieClicked(movieId: Int) {
        _navigationState.tryEmit(FeedNavigationState.MovieDetails(movieId))
    }

    fun onLoadStateUpdate(loadStates: CombinedLoadStates) {
        val showLoading = loadStates.refresh is LoadState.Loading

        val error = when (val refresh = loadStates.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(showLoading = showLoading, errorMessage = error) }
    }

    fun observeNetworkState() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }


}