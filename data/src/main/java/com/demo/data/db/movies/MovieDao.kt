package com.demo.data.db.movies

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.data.entities.MovieDBData

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies_table ORDER BY category")
    fun movies(): PagingSource<Int, MovieDBData>

    @Query("SELECT * FROM movies_table ORDER BY category")
    fun getMovies(): List<MovieDBData>

    @Query("SELECT * FROM movies_table WHERE id=:movieId")
    fun getMovie(movieId: Int): MovieDBData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(movieDBData: List<MovieDBData>)

    @Query("DELETE FROM movies_table WHERE id NOT IN (SELECT movieId FROM favorite_movies_table)")
    fun clearMoviesExceptFavorite()
}