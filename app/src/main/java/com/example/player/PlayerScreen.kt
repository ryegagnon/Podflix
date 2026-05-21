package com.example.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.data.Podcast

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    podcast: Podcast,
    onNavigateBack: () -> Unit,
    onSaveProgress: (podcastId: String, currentPosition: Long, duration: Long) -> Unit
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(podcast.videoUrl)
            setMediaItem(mediaItem)
            prepare()
            seekTo(podcast.playbackPositionMs)
            playWhenReady = true
        }
    }

    LaunchedEffect(exoPlayer) {
        // Here we could add listeners if we wanted, but we'll just save progress on dispose
    }

    DisposableEffect(Unit) {
        onDispose {
            val currentPosition = exoPlayer.currentPosition
            val duration = exoPlayer.duration
            if (duration > 0) {
                onSaveProgress(podcast.id, currentPosition, duration)
            }
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                // Typical TV optimization
                controllerShowTimeoutMs = 3000
                controllerHideOnTouch = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
