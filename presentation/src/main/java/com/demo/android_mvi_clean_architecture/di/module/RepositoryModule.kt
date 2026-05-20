package com.demo.android_mvi_clean_architecture.di.module

import com.demo.data.MovieRemoteDataSource
import com.demo.data.repository.movie.MovieDataLocalSource
import com.demo.data.repository.movie.MovieDataSource
import com.demo.data.repository.movie.MovieRepositoryImpl
import com.demo.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun provideMovieRemoteDataSource(
        movieRemoteDataSource: MovieRemoteDataSource
    ): MovieDataSource.Remote

    @Binds
    abstract fun provideMovieLocalDataSource(
        movieDataLocalSource: MovieDataLocalSource
    ): MovieDataSource.Local


}