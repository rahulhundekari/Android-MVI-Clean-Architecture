package com.demo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.domain.entities.MovieEntity

@Entity(tableName = "movies_table")
data class MovieDBData(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val backgroundUrl: String,
    val category: String
)

fun MovieDBData.toDomain() = MovieEntity(
    id = id,
    title = title,
    description = description,
    image = image,
    backgroundUrl = backgroundUrl,
    category = category
)