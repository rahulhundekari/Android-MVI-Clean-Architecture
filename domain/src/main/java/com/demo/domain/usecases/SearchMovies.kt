package com.demo.domain.usecases

import com.demo.domain.repository.MovieRepository

class SearchMovies(private val movieRepository: MovieRepository) {
    operator fun invoke(query: String, pagesize: Int) = movieRepository.search(query, pagesize)

}