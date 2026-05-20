package com.demo.android_mvi_clean_architecture.mapper

import com.demo.android_mvi_clean_architecture.entities.MovieListItem
import com.demo.domain.entities.MovieEntity


fun MovieEntity.toPresentation() = MovieListItem.Movie(
    id = id,
    imageUrl = image,
    category = category
)

fun MovieEntity.toMovieListItem(): MovieListItem = this.toPresentation()