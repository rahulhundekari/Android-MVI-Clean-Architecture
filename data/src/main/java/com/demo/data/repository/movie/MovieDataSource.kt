package com.demo.data.repository.movie

import androidx.paging.PagingSource
import com.demo.data.entities.MovieDBData
import com.demo.data.entities.MovieData
import com.demo.data.entities.MovieRemoteKeyDBData
import com.demo.domain.entities.MovieEntity
import com.demo.domain.util.Result

interface MovieDataSource {

    interface Remote {
        suspend fun getMovies(pageSize: Int, limit: Int): Result<List<MovieData>>
        suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>>
        suspend fun getMovie(movieId: Int): Result<MovieData>
        suspend fun search(query: String, pageSize: Int, limit: Int): Result<List<MovieData>>
    }

    interface Local {
        fun movies(): PagingSource<Int, MovieDBData>
        suspend fun getMovies(): Result<List<MovieEntity>>
        suspend fun getMovie(movieId: Int): Result<MovieEntity>
        suspend fun saveMovies(movies: List<MovieData>)
        suspend fun getLastRemoteKey(): MovieRemoteKeyDBData?
        suspend fun setMovieRemoteKey(key: MovieRemoteKeyDBData)
        suspend fun clearMovies()
        suspend fun clearMovieRemoteKeys()
    }
}