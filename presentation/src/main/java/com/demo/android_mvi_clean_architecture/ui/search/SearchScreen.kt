package com.demo.android_mvi_clean_architecture.ui.search

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.demo.android_mvi_clean_architecture.R
import com.demo.android_mvi_clean_architecture.entities.MovieListItem
import com.demo.android_mvi_clean_architecture.navigation.Screen
import com.demo.android_mvi_clean_architecture.ui.feed.MovieList
import com.demo.android_mvi_clean_architecture.util.collectAsEffect
import com.demo.android_mvi_clean_architecture.util.preview.PreviewContainer
import com.demo.android_mvi_clean_architecture.widget.EmptyStateIcon
import com.demo.android_mvi_clean_architecture.widget.EmptyStateView
import com.demo.android_mvi_clean_architecture.widget.LoaderFullScreen
import com.demo.android_mvi_clean_architecture.widget.SearchView
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchScreen(
    mainNavHostController: NavHostController,
    viewModel: SearchViewModel
) {

    val uiState by viewModel.searchUiState.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    viewModel.onLoadStateUpdate(movies.loadState, movies.itemCount)

    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is SearchNavigationState.MovieDetails -> {
                mainNavHostController.navigate(Screen.MovieDetails(navigationState.movieId))
            }
        }
    }

    SearchView(
        searchUiState = uiState,
        movies = movies,
        onMovieClick = { movieId ->
            viewModel.onMovieClicked(movieId)
        },
        onQueryChange = viewModel::onSearch,
        onBackClick = {
            mainNavHostController.popBackStack()
        }
    )

}

@Composable
fun SearchView(
    searchUiState: SearchUiState,
    movies: LazyPagingItems<MovieListItem>,
    onMovieClick: (movieId: Int) -> Unit,
    onQueryChange: (query: String) -> Unit,
    onBackClick: () -> Unit
) {
    Surface {
        val context = LocalContext.current
        val showDefaultState = searchUiState.showDefaultState
        val showNoMoviesFound = searchUiState.showNoMoviesFound
        val isLoading = searchUiState.showLoading
        val errorMessage = searchUiState.errorMessage
        var query: String by remember { mutableStateOf("") }

        if (errorMessage != null) Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

        Scaffold(
            topBar = {
                SearchView(
                    onQueryChange = onQueryChange,
                    onBackClick = onBackClick
                )
            }
        ) {
            Box(Modifier.padding(it)) {
                if (showDefaultState) {
                    EmptyStateView(
                        title = stringResource(R.string.first_time_search_title),
                        icon = EmptyStateIcon(
                            iconRes = R.drawable.bg_empty_search,
                            size = 100.dp,
                            spacing = 12.dp
                        ),
                        subTitle = stringResource(R.string.first_time_search_subtitle),
                        titleTextSize = 20.sp,
                        subTitleTextSize = 16.sp,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(top = 80.dp, start = 24.dp, end = 24.dp)
                    )
                } else {
                    if (isLoading) {
                        LoaderFullScreen(
                            modifier = Modifier.padding(150.dp),
                            alignment = Alignment.TopCenter
                        )
                    } else {
                        if (showNoMoviesFound) {
                            EmptyStateView(
                                title = stringResource(R.string.no_search_results_title),
                                icon = EmptyStateIcon(
                                    iconRes = R.drawable.bg_empty_no_result,
                                    size = 100.dp,
                                    spacing = 12.dp
                                ),
                                subTitle = stringResource(
                                    R.string.no_search_results_subtitle,
                                    query
                                ),
                                titleTextSize = 20.sp,
                                subTitleTextSize = 16.sp,
                                verticalArrangement = Arrangement.Top,
                                modifier = Modifier.padding(top = 80.dp, start = 24.dp, end = 24.dp)
                            )
                        } else {
                            MovieList(
                                movies = movies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }
                }
            }
        }

    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    val movieItems: List<MovieListItem> = listOf(
        MovieListItem.Separator("Action"),
        MovieListItem.Movie(1, "image1.jpg", "Action"),
        MovieListItem.Movie(2, "image2.jpg", "Comedy"),
        MovieListItem.Separator("Drama"),
        MovieListItem.Movie(3, "image3.jpg", "Drama")
    )

    PreviewContainer {
        val movies = flowOf(PagingData.from(movieItems)).collectAsLazyPagingItems()
        SearchView(
            SearchUiState(
                showDefaultState = false,
                showNoMoviesFound = true
            ), movies, {}, {}, {})
    }
}