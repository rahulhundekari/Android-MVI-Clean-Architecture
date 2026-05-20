package com.demo.data.repository.movie.favorite

import androidx.paging.PagingSource
import com.demo.data.db.favoritemovies.FavoriteMovieDao
import com.demo.data.entities.FavoriteMovieDBData
import com.demo.data.entities.MovieDBData
import com.demo.data.exception.DataNotFoundException
import com.demo.domain.util.Result

class FavoriteMoviesLocalDataSource(
    private val favoriteMovieDao: FavoriteMovieDao
) : FavoriteMoviesDataSource.Local {

    override fun favoriteMovies(): PagingSource<Int, MovieDBData> =
        favoriteMovieDao.favoriteMovies()

    override suspend fun getFavoriteMovieIds(): Result<List<Int>> {
        val movieIds = favoriteMovieDao.getAll().map { it.movieId }
        return if (movieIds.isNotEmpty()) {
            Result.Success(movieIds)
        } else {
            Result.Error(DataNotFoundException())
        }
    }

    override suspend fun addMovieToFavorite(movieId: Int) {
        favoriteMovieDao.add(FavoriteMovieDBData(movieId))
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) {
        favoriteMovieDao.remove(movieId)
    }

    override suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean> {
        return Result.Success(favoriteMovieDao.getFavoriteMovie(movieId) != null)
    }
}