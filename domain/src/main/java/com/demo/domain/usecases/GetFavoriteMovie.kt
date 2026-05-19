package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class GetFavoriteMovie(private val movieRepository: MovieRepository) {
    operator fun invoke(movieId: Int) = movieRepository.favoriteMovie(movieId)
}