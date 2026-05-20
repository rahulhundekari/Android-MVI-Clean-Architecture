package com.demo.data

import com.demo.data.api.MovieApi
import com.demo.data.entities.MovieData
import com.demo.data.repository.movie.MovieDataSource
import com.demo.data.utils.safeApiCall
import com.demo.domain.util.Result
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) : MovieDataSource.Remote {
    override suspend fun getMovies(
        pageSize: Int,
        limit: Int
    ): Result<List<MovieData>> = safeApiCall {
        movieApi.getMovies(pageSize, limit)
    }

    override suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>> = safeApiCall {
        movieApi.getMovies(movieIds)
    }

    override suspend fun getMovie(movieId: Int): Result<MovieData> = safeApiCall {
        movieApi.getMovie(movieId)
    }

    override suspend fun search(
        query: String,
        pageSize: Int,
        limit: Int
    ): Result<List<MovieData>> = safeApiCall {
        movieApi.search(query, pageSize, limit)
    }
}