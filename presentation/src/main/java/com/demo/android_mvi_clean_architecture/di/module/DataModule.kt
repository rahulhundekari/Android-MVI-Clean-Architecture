package com.demo.android_mvi_clean_architecture.di.module

import com.demo.data.db.favoritemovies.FavoriteMovieDao
import com.demo.data.repository.movie.MovieDataSource
import com.demo.data.repository.movie.MovieRemoteMediator
import com.demo.data.repository.movie.favorite.FavoriteMoviesDataSource
import com.demo.data.repository.movie.favorite.FavoriteMoviesLocalDataSource
import com.demo.domain.repository.MovieRepository
import com.demo.domain.usecases.AddMovieToFavorite
import com.demo.domain.usecases.CheckFavoriteStatus
import com.demo.domain.usecases.GetFavoriteMovie
import com.demo.domain.usecases.GetMovieDetails
import com.demo.domain.usecases.RemoveMovieFromFavorite
import com.demo.domain.usecases.SearchMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMovieRemoteMediator(
        localMovieDataSource: MovieDataSource.Local,
        remoteMovieDataSource: MovieDataSource.Remote
    ): MovieRemoteMediator {
        return MovieRemoteMediator(localMovieDataSource, remoteMovieDataSource)
    }

    @Singleton
    @Provides
    fun provideFavoriteMovieLocalDataSource(
        favoriteMovieDao: FavoriteMovieDao
    ): FavoriteMoviesDataSource.Local {
        return FavoriteMoviesLocalDataSource(favoriteMovieDao)
    }

    @Provides
    @Singleton
    fun provideSearchMovieUseCase(searchMovieRepository: MovieRepository): SearchMovies {
        return SearchMovies(searchMovieRepository)
    }

    @Provides
    fun provideGetMovieDetailsUseCase(movieRepository: MovieRepository): GetMovieDetails {
        return GetMovieDetails(movieRepository)
    }

    @Provides
    fun provideGetFavoriteMoviesUseCase(movieRepository: MovieRepository): GetFavoriteMovie {
        return GetFavoriteMovie(movieRepository)
    }

    @Provides
    fun provideCheckFavoriteStatusUseCase(movieRepository: MovieRepository): CheckFavoriteStatus {
        return CheckFavoriteStatus(movieRepository)
    }

    @Provides
    fun provideAddMovieToFavoriteUseCase(movieRepository: MovieRepository): AddMovieToFavorite {
        return AddMovieToFavorite(movieRepository)
    }

    @Provides
    fun provideRemoveMovieFromFavoriteUseCase(movieRepository: MovieRepository): RemoveMovieFromFavorite {
        return RemoveMovieFromFavorite(movieRepository)
    }
}