package com.demo.data.repository.movie

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.demo.data.entities.MovieDBData
import com.demo.data.entities.MovieRemoteKeyDBData
import com.demo.domain.util.Result

private const val MOVIE_STARTING_PAGE_INDEX = 1


@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val local: MovieDataSource.Local,
    private val remote: MovieDataSource.Remote
) : RemoteMediator<Int, MovieDBData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDBData>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> MOVIE_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = local.getLastRemoteKey()
                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        Log.d(
            "MovieRemoteMediator",
            "MovieRemoteMediator: load() called with: loadType = $loadType, page: $page, stateLastItem = ${state.isEmpty()}"
        )

        // There was a lag in loading the first page; as a result, it jumps to the end of the pagination.
        if (state.isEmpty() && page == 2) return MediatorResult.Success(endOfPaginationReached = false)

        when (val result = remote.getMovies(page, state.config.pageSize)) {
            is Result.Success -> {
                Log.d("MovieRemoteMediator", "MovieRemoteMediator: get movies from remote")

                if (loadType == LoadType.REFRESH) {
                    local.clearMovies()
                    local.clearMovieRemoteKeys()
                }

                val movies = result.data

                val endOfPaginationReached = movies.isEmpty()

                val nextPage = if (endOfPaginationReached) null else (page + 1)
                val prevPage = if (page == MOVIE_STARTING_PAGE_INDEX) null else (page - 1)

                val key = MovieRemoteKeyDBData(nextPage = nextPage, prevPage = prevPage)
                local.saveMovies(movies)
                local.setMovieRemoteKey(key)

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            is Result.Error -> return MediatorResult.Error(result.error)
        }
    }
}