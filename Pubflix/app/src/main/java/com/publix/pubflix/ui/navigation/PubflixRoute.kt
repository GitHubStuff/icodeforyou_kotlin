package com.publix.pubflix.ui.navigation

import kotlinx.serialization.Serializable

sealed interface PubflixRoute {

    @Serializable
    data object Splash : PubflixRoute

    @Serializable
    data object Search : PubflixRoute

    @Serializable
    data class MovieDetail(
        val title: String,
        val year: String,
        val poster: String?,
    ) : PubflixRoute
}