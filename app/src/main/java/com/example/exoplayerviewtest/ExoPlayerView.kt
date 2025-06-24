package com.example.exoplayerviewtest

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun ExoPlayerView(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context)
            )
            .build().apply {
                volume = 0f
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_ALL

                val mediaItem = MediaItem
                    .Builder()
                    .setUri(videoUrl.toUri())
                    .setLiveConfiguration(
                        MediaItem
                            .LiveConfiguration
                            .Builder()
                            .setMaxPlaybackSpeed(1f)
                            .build()
                    )
                    .build()
                setMediaItem(mediaItem)
                prepare()
            }
        }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = modifier
    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
} 