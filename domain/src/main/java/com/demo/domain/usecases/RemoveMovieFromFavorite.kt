package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class RemoveMovieFromFavorite(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = movieRepository.removeMovieFromFavorite(movieId)
}