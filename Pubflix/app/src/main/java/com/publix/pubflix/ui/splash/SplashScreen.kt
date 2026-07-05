package com.publix.pubflix.ui.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.publix.pubflix.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Delay time (in milliseconds) for the splash screen
const val splash_duration: Long = 2400

@Composable
fun SplashScreen(
    onFinished: ()-> Unit
){
    LaunchedEffect(Unit){
        delay(splash_duration.milliseconds)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.pubflix2048),
            contentDescription = "Pubflix Logo",
            contentScale = ContentScale.Fit
        )
    }
}
