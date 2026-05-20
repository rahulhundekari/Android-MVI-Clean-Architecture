package com.demo.android_mvi_clean_architecture.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object NavigationBar : Screen()

    @Serializable
    data object Feed : Screen()

    @Serializable
    data object Favorites : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data class MovieDetails(val movieId: Int) : Screen()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}

fun Screen.route(): String? {
    return this.javaClass.canonicalName
}