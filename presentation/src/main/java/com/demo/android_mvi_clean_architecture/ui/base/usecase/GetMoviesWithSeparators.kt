package com.demo.android_mvi_clean_architecture.ui.base.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.demo.android_mvi_clean_architecture.entities.MovieListItem
import com.demo.android_mvi_clean_architecture.mapper.toPresentation
import com.demo.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoviesWithSeparators @Inject constructor(
    private val movieRepository: MovieRepository,
    private val insertSeparatorIntoPagingData: InsertSeparatorIntoPagingData
) {

    fun movies(pageSize: Int): Flow<PagingData<MovieListItem>> = movieRepository.movies(pageSize).map {
        val pagingData: PagingData<MovieListItem.Movie> = it.map { movie -> movie.toPresentation() }
        insertSeparatorIntoPagingData.insert(pagingData)
    }
}
