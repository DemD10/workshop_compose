package com.app.composeworkshop

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun EffectsScreen(
    videoProgress: Long,
    duration: Long
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ) {
        val screenWidthPx = with(LocalDensity.current) {
            (LocalConfiguration.current.screenWidthDp * density)
        }
        val animateFloat = remember { Animatable(0f) }

        LaunchedEffect(animateFloat) {
            animateFloat.animateTo(
                targetValue = videoProgress * screenWidthPx / duration,
                animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing))
        }

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .clip(MaterialTheme.shapes.small)
            .height(50.dp)
            .background(MaterialTheme.colors.primary)
        ) {

            drawRect(
                color = Color.Green,
                size = Size(animateFloat.value, size.height)
            )
        }

        Text(
            text = "Video progress ${(animateFloat.value / screenWidthPx * 100).toInt()}%",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}