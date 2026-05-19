package com.demo.data.db

import androidx.room.Database
import com.demo.data.db.favoritemovies.FavoriteMovieDao
import com.demo.data.db.movies.MovieDao
import com.demo.data.db.movies.MovieRemoteKeyDao
import com.demo.data.entities.FavoriteMovieDBData
import com.demo.data.entities.MovieDBData
import com.demo.data.entities.MovieRemoteKeyDBData

@Database(
    entities = [MovieDBData::class, FavoriteMovieDBData::class, MovieRemoteKeyDBData::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase {
    abstract fun moviesDao() : MovieDao
    abstract fun favoriteMoviesDao(): FavoriteMovieDao
    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao
}