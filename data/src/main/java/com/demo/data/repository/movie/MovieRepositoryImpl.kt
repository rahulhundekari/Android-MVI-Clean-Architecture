package com.demo.data.repository.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.demo.data.entities.toDomain
import com.demo.data.repository.movie.favorite.FavoriteMoviesDataSource
import com.demo.domain.entities.MovieEntity
import com.demo.domain.repository.MovieRepository
import com.demo.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.demo.domain.util.Result
import com.demo.domain.util.onError
import com.demo.domain.util.onSuccess

class MovieRepositoryImpl(
    private val local: MovieDataSource.Local,
    private val remote: MovieDataSource.Remote,
    private val remoteMediator: MovieRemoteMediator,
    private val localFavorite: FavoriteMoviesDataSource.Local
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun movies(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { local.movies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun favoriteMovie(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { localFavorite.favoriteMovies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override fun search(
        query: String,
        pageSize: Int
    ): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SearchMoviePagingSource(query, remote) }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> {
        return when (val result = local.getMovie(movieId)) {
            is Result.Success -> result
            is Result.Error -> remote.getMovie(movieId).map { it.toDomain() }
        }
    }

    override suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean> =
        localFavorite.checkFavoriteStatus(movieId)

    override suspend fun addMovieToFavorite(movieId: Int) {
        local.getMovie(movieId)
            .onSuccess {
                localFavorite.addMovieToFavorite(movieId)
            }
            .onError {
                remote.getMovie(movieId)
                    .onSuccess { movie ->
                        local.saveMovies(listOf(movie))
                        localFavorite.addMovieToFavorite(movieId)
                    }
            }
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) =
        localFavorite.removeMovieFromFavorite(movieId)


    override suspend fun sync(): Boolean {
        return when (val result = local.getMovies()) {
            is Result.Success -> {
                val movieIds = result.data.map { it.id }
                updateLocalWithRemoteMovies(movieIds)
            }

            is Result.Error -> false
        }
    }

    private suspend fun updateLocalWithRemoteMovies(movieIds: List<Int>): Boolean {
        return when (val result = remote.getMovies(movieIds)) {
            is Result.Error -> false
            is Result.Success -> {
                local.saveMovies(result.data)
                true
            }
        }
    }
}