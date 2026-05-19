package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class GetMovieDetails(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(pageSize: Int) = movieRepository.getMovie(pageSize)
}