package com.demo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies_table")
data class FavoriteMovieDBData(
    @PrimaryKey val movieId: Int
)
