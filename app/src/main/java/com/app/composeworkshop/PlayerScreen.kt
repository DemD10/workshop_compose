package com.app.composeworkshop

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val exoPlayer = initExoPlayerAsRemember(context, viewState.value.playbackPosition).apply {
        playWhenReady = viewState.value.isPlaying && !viewState.value.isManuallyPaused
    }

    PlayerScreenContent(
        viewState = viewState.value,
        exoPlayer = exoPlayer,
        onAction = {
            viewModel.onAction(it)
        }
    )

    val lifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.onAction(PlayerViewModel.UiAction.LifecycleActionResume)
            Lifecycle.Event.ON_PAUSE -> viewModel.onAction(PlayerViewModel.UiAction.LifecycleActionPause)
            else -> {
                // any code
            }
        }
    }

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            viewModel.onAction(PlayerViewModel.UiAction.SavePlaybackPosition(exoPlayer.currentPosition))
            exoPlayer.release()
        }
    }
}

@Composable
fun PlayerScreenContent(
    viewState: PlayerViewModel.ViewState,
    exoPlayer: SimpleExoPlayer,
    onAction: (PlayerViewModel.UiAction) -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
    ) {

        VideoPlayer(
            exoPlayer = exoPlayer,
            onPlayerAction = {
                onAction.invoke(PlayerViewModel.UiAction.PlayerClick)
            }
        )

        val alpha: Float by animateFloatAsState(
            if (viewState.isManuallyPaused) 1f else 0f,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_round_play),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(alpha = alpha)
                .align(Alignment.Center)
                .size(120.dp, 120.dp)
        )

        Row(modifier = Modifier
            .align(Alignment.BottomStart)
        ) {
            EditorButton(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                        onAction.invoke(PlayerViewModel.UiAction.EffectsClick(
                            videoProgress = exoPlayer.currentPosition,
                            duration = exoPlayer.duration
                        ))
                    },
                image = R.drawable.ic_music,
                text = "Музыка"
            )
            EditorButton(
                modifier = Modifier
                    .padding(start = 16.dp),
                image = R.drawable.ic_add_effect,
                text = "Эффекты"
            )
            EditorButton(
                modifier = Modifier
                    .padding(start = 16.dp),
                image = R.drawable.ic_text,
                text = "Текст"
            )
            EditorButton(
                modifier = Modifier
                    .padding(start = 16.dp),
                image = R.drawable.ic_sticker,
                text = "Стикеры"
            )
        }
    }
}

@Composable
fun VideoPlayer(
    exoPlayer: SimpleExoPlayer,
    onPlayerAction: (isPlaying: Boolean) -> Unit
) {
    AndroidView(
        factory = { viewContext ->
            PlayerView(viewContext).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            }
        },
        modifier = Modifier
            .padding(bottom = 60.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onPlayerAction.invoke(exoPlayer.isPlaying)
            }
            .fillMaxSize()
    )
}

@Composable
fun initExoPlayerAsRemember(
    context: Context,
    position: Long
) = remember {
    SimpleExoPlayer.Builder(context).build().apply {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
            Util.getUserAgent(context, context.packageName))

        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri("asset:///temp.mp4"))

        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        setMediaSource(source)
        seekTo(position)
        repeatMode = Player.REPEAT_MODE_ALL
        prepare()
    }
}