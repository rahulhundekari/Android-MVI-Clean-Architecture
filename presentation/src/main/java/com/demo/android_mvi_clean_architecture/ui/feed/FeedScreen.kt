package com.demo.android_mvi_clean_architecture.ui.feed

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.demo.android_mvi_clean_architecture.R
import com.demo.android_mvi_clean_architecture.entities.MovieListItem
import com.demo.android_mvi_clean_architecture.navigation.Screen
import com.demo.android_mvi_clean_architecture.ui.main.MainRouter
import com.demo.android_mvi_clean_architecture.ui.navigatiobar.NavigationBarSharedViewModel
import com.demo.android_mvi_clean_architecture.ui.theme.colors
import com.demo.android_mvi_clean_architecture.util.ImageSize
import com.demo.android_mvi_clean_architecture.util.collectAsEffect
import com.demo.android_mvi_clean_architecture.util.toPX
import com.demo.android_mvi_clean_architecture.widget.Loader
import com.demo.android_mvi_clean_architecture.widget.LoaderFullScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    mainRouter: MainRouter,
    viewModel: FeedViewModel,
    sharedViewModel: NavigationBarSharedViewModel
) {

    val moviesPaging = viewModel.movies.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val lazyGridState = rememberLazyGridState()

    viewModel.navigationState.collectAsEffect { navigateState ->
        when (navigateState) {
            is FeedNavigationState.MovieDetails -> mainRouter.navigateToMovieDetails(navigateState.movieId)
        }
    }

    viewModel.refreshListState.collectAsEffect {
        moviesPaging.refresh()
    }

    sharedViewModel.bottomItem.collectAsEffect {
        if (it.screen == Screen.Feed) {
            lazyGridState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = moviesPaging.loadState) {
        viewModel.onLoadStateUpdate(moviesPaging.loadState)
    }

    PullToRefreshBox(
        isRefreshing = uiState.showLoading,
        onRefresh = { viewModel.onRefresh() },
    ) {
        FeedView(
            movies = moviesPaging,
            uiState = uiState,
            lazyGridState = lazyGridState,
            onMovieClick = viewModel::onMovieClicked
        )
    }

}

@Composable
fun FeedView(
    movies: LazyPagingItems<MovieListItem>,
    uiState: FeedUiState,
    lazyGridState: LazyGridState,
    onMovieClick: (Int) -> Unit
) {
    Surface {
        if (uiState.showLoading) {
            LoaderFullScreen()
        } else {
            MovieList(movies, lazyGridState, onMovieClick)
        }
    }

}


@Composable
fun MovieList(
    movies: LazyPagingItems<MovieListItem>,
    lazyGridState: LazyGridState,
    onMovieClick: (Int) -> Unit,
    config: MovieSpanSizeConfig = MovieSpanSizeConfig(3)
) {
    val imageSize = ImageSize.getImageFixedSize()
    LazyVerticalGrid(
        modifier = Modifier.background(color = colors.background),
        columns = GridCells.Fixed(config.gridSpanSize),
        state = lazyGridState
    ) {
        items(
            movies.itemCount,
            span = { index ->
                val spinSize = when (movies[index]) {
                    is MovieListItem.Movie -> config.movieColumnSpanSize
                    is MovieListItem.Separator -> config.separatorColumnSpanSize
                    null -> config.footerColumnSpanSize
                }
                GridItemSpan(spinSize)
            }) { index ->

            val itemVisible by remember {
                derivedStateOf {
                    val visibleItems = lazyGridState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }

            when (val movie = movies[index]) {
                is MovieListItem.Movie -> MovieItem(
                    movie = movie,
                    imageSize = imageSize,
                    itemVisible = itemVisible,
                    onMovieClick = onMovieClick
                )

                is MovieListItem.Separator -> Separator(movie.category)
                null -> Loader()
            }
        }

    }
}


@Composable
fun MovieItem(
    movie: MovieListItem.Movie,
    imageSize: ImageSize,
    itemVisible: Boolean,
    onMovieClick: (Int) -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(0.70f) }
    val animatedScale by animateFloatAsState(targetValue = scale, label = "FloatAnimation")

    LaunchedEffect(itemVisible) {
        if (itemVisible) {
            delay(100)
            scale = 1f
        } else {
            scale = 0.70f
        }
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movie.imageUrl)
            .scale(Scale.FILL)
            .size(imageSize.width.toPX(), imageSize.width.toPX())
            .build(),
        loading = { MovieItemPlaceHolder() },
        error = { MovieItemPlaceHolder() },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(3.dp)
            .aspectRatio(9 / 16f)
            .scale(animatedScale)
            .clip(RoundedCornerShape(2.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        scale = 0.90f
                        tryAwaitRelease()
                        scale = 1.0f
                    },
                    onTap = {
                        onMovieClick(movie.id)
                    }
                )
            })
}

@Composable
fun MovieItemPlaceHolder() {
    Image(
        painter = painterResource(id = R.drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}

/**
 * @property gridSpanSize - The total number of columns in the grid.
 * @property separatorColumnSpanSize - Returns the number of columns that the item occupies.
 * @property footerColumnSpanSize - Returns the number of columns that the item occupies.
 **/
data class MovieSpanSizeConfig(val gridSpanSize: Int) {
    val movieColumnSpanSize: Int = 1
    val separatorColumnSpanSize: Int = gridSpanSize
    val footerColumnSpanSize: Int = gridSpanSize
}

@Composable
fun Separator(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )
}
