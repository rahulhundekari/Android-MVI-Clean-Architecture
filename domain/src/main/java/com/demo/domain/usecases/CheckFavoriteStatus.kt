package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class CheckFavoriteStatus(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = movieRepository.checkFavoriteStatus(movieId)
}