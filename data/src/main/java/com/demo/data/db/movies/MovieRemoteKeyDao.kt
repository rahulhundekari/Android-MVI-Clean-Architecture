package com.demo.data.db.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demo.data.entities.MovieRemoteKeyDBData

@Dao
interface MovieRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKey(keys: MovieRemoteKeyDBData)

    @Query("SELECT * FROM remote_key_table WHERE id=:movieId")
    suspend fun getRemoteKeyByMovieId(movieId: Int): MovieRemoteKeyDBData?

    @Query("DELETE FROM remote_key_table")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_key_table WHERE id = (SELECT MAX(id) FROM remote_key_table)")
    suspend fun getLastRemoteKey(): MovieRemoteKeyDBData?
}