package com.demo.data.entities

import com.demo.domain.entities.MovieEntity
import com.google.gson.annotations.SerializedName

data class MovieData(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("backgroundUrl") val backgroundUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
)

fun MovieData.toDomain(): MovieEntity = MovieEntity(
    id = id,
    description = description,
    title = title,
    image = image,
    category = category,
    backgroundUrl = backgroundUrl,
)

fun MovieData.toMovieDbData(): MovieDBData = MovieDBData(
    id = id,
    description = description,
    image = image,
    backgroundUrl = backgroundUrl,
    title = title,
    category = category,
)