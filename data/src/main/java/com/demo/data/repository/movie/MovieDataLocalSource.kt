package com.demo.data.repository.movie

import androidx.paging.PagingSource
import com.demo.data.db.movies.MovieDao
import com.demo.data.db.movies.MovieRemoteKeyDao
import com.demo.data.entities.MovieDBData
import com.demo.data.entities.MovieData
import com.demo.data.entities.MovieRemoteKeyDBData
import com.demo.data.entities.toDomain
import com.demo.data.entities.toMovieDbData
import com.demo.data.exception.DataNotFoundException
import com.demo.domain.entities.MovieEntity
import com.demo.domain.util.Result

class MovieDataLocalSource(
    private val movieDao: MovieDao,
    private val remoteDao: MovieRemoteKeyDao
) : MovieDataSource.Local {
    override fun movies(): PagingSource<Int, MovieDBData> = movieDao.movies()

    override suspend fun getMovies(): Result<List<MovieEntity>> {
        val movies = movieDao.getMovies()
        return if (movies.isNotEmpty()) {
            Result.Success(movies.map { it.toDomain() })
        } else {
            Result.Error(DataNotFoundException())
        }
    }

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> {
        return movieDao.getMovie(movieId)?.let {
            Result.Success(it.toDomain())
        } ?: Result.Error(DataNotFoundException())
    }

    override suspend fun saveMovies(movies: List<MovieData>) {
        movieDao.saveMovies(movies.map { it.toMovieDbData() })
    }

    override suspend fun getLastRemoteKey(): MovieRemoteKeyDBData? = remoteDao.getLastRemoteKey()

    override suspend fun setMovieRemoteKey(key: MovieRemoteKeyDBData) {
        remoteDao.saveRemoteKey(key)
    }

    override suspend fun clearMovies() {
        movieDao.clearMoviesExceptFavorite()
    }

    override suspend fun clearMovieRemoteKeys() {
        remoteDao.clearRemoteKeys()
    }
}