package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class AddMovieToFavorite(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = movieRepository.addMovieToFavorite(movieId)
}