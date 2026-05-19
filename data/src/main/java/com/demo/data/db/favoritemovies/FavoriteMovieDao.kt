package com.demo.data.db.favoritemovies

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.data.entities.FavoriteMovieDBData
import com.demo.data.entities.MovieDBData

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM movies_table WHERE id in (SELECT movieId FROM favorite_movies_table)")
    fun favoriteMovies(): PagingSource<Int, MovieDBData>

    @Query("SELECT * FROM favorite_movies_table")
    suspend fun getAll(): List<FavoriteMovieDBData>

    @Query("SELECT * FROM favorite_movies_table WHERE movieId=:movieId")
    suspend fun getFavoriteMovie(movieId: Int): FavoriteMovieDBData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favoriteMovieDBData: FavoriteMovieDBData)

    @Query("DELETE FROM favorite_movies_table WHERE movieId=:movieId")
    suspend fun remove(movieId: Int)
}