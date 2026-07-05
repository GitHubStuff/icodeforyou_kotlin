package com.publix.pubflix.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.publix.pubflix.ui.detail.MovieDetailScreen
import com.publix.pubflix.ui.navigation.PubflixRoute
import com.publix.pubflix.ui.search.SearchScreen
import com.publix.pubflix.ui.splash.SplashScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PubflixRoute.Splash,
    ) {
        composable<PubflixRoute.Splash> {
            SplashScreen(
                onFinished = {
                    navController.navigate(PubflixRoute.Search) {
                        // Drop Splash from the back stack so Back exits the app
                        // rather than returning to the logo.
                        popUpTo(PubflixRoute.Splash) { inclusive = true }
                    }
                },
            )
        }

        composable<PubflixRoute.Search> {
            SearchScreen(
                onMovieClick = { movie ->
                    navController.navigate(
                        PubflixRoute.MovieDetail(
                            title = movie.title,
                            year = movie.year,
                            poster = movie.poster,
                        ),
                    )
                },
            )
        }

        composable<PubflixRoute.MovieDetail> { backStackEntry ->
            val detail = backStackEntry.toRoute<PubflixRoute.MovieDetail>()
            MovieDetailScreen(
                title = detail.title,
                year = detail.year,
                poster = detail.poster,
                onBack = navController::navigateUp,
            )
        }
    }
}