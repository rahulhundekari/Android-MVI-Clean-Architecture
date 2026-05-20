package com.demo.android_mvi_clean_architecture.di.module

import android.content.Context
import androidx.room.Room
import com.demo.data.db.MovieDatabase
import com.demo.data.db.favoritemovies.FavoriteMovieDao
import com.demo.data.db.movies.MovieDao
import com.demo.data.db.movies.MovieRemoteKeyDao
import com.demo.data.utils.DiskExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): MovieDatabase {
        return Room
            .databaseBuilder(context, MovieDatabase::class.java, "movie.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
            .build()
    }

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao = movieDatabase.moviesDao()


    @Provides
    fun provideFavoriteMoviesDao(movieDatabase: MovieDatabase): FavoriteMovieDao =
        movieDatabase.favoriteMoviesDao()

    @Provides
    fun provideMovieRemoteKeyDao(movieDatabase: MovieDatabase): MovieRemoteKeyDao =
        movieDatabase.movieRemoteKeyDao()

}