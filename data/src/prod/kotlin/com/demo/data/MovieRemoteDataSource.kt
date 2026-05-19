package com.demo.data

import com.demo.data.api.MovieApi
import com.demo.data.entities.MovieData
import com.demo.data.repository.movie.MovieDataSource
import com.demo.data.utils.SafeApiCall
import com.demo.domain.util.Result

class MovieRemoteDataSource(
    private val movieApi: MovieApi
) : MovieDataSource.Remote {
    override suspend fun getMovies(
        pageSize: Int,
        limit: Int
    ): Result<List<MovieData>> = SafeApiCall {
        movieApi.getMovies(pageSize, limit)
    }

    override suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>> = SafeApiCall {
        movieApi.getMovies(movieIds)
    }

    override suspend fun getMovie(movieId: Int): Result<MovieData> = SafeApiCall {
        movieApi.getMovie(movieId)
    }

    override suspend fun search(
        query: String,
        pageSize: Int,
        limit: Int
    ): Result<List<MovieData>> = SafeApiCall {
        movieApi.search(query, pageSize, limit)
    }
}