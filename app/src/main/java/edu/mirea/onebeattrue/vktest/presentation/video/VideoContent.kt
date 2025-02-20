package edu.mirea.onebeattrue.vktest.presentation.video

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import edu.mirea.onebeattrue.vktest.presentation.applicationComponent

@OptIn(UnstableApi::class)
@Composable
fun VideoContent(
    component: VideoComponent,
    modifier: Modifier = Modifier,
) {
    val exoPlayer = applicationComponent.getPlayer()

    LaunchedEffect(Unit) {
        exoPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri(component.video.videoUrl)
                .build()
        )
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
