package edu.mirea.onebeattrue.vktest.presentation.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import edu.mirea.onebeattrue.vktest.R
import edu.mirea.onebeattrue.vktest.domain.model.Video
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(component.labels) {
        component.labels
            .collect { label ->
                when (label) {
                    is MainStore.Label.ShowError -> Toast.makeText(
                        context,
                        label.error.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
            }
    }

    Scaffold {
        PullToRefreshBox(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            onRefresh = component::refresh,
            isRefreshing = state.loadingState == MainStore.State.LoadingState.Refreshing,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = state.videos,
                    key = { it.id }
                ) { video ->
                    VideoItem(
                        video = video,
                        onItemClicked = component::clickVideo,
                        modifier = Modifier.animateItem()
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (state.loadingState) {
                            MainStore.State.LoadingState.NeedLoadNext -> {
                                LaunchedEffect(state.loadingState) {
                                    component.loadNext()
                                }
                            }

                            MainStore.State.LoadingState.Error -> {
                                TextButton(
                                    onClick = component::loadNext,
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.Gray
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(stringResource(R.string.retry))
                                }
                            }

                            MainStore.State.LoadingState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoItem(
    video: Video,
    onItemClicked: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        onClick = { onItemClicked(video) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(video.width.toFloat() / video.height)
                    .background(Color.Gray),
                model = video.thumbnail,
                contentDescription = "thumbnail",
                contentScale = ContentScale.Crop
            )
            DurationCard(video.duration)
        }
        Text(
            modifier = Modifier.padding(16.dp),
            text = video.author,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BoxScope.DurationCard(
    duration: Int,
) {
    Card(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = .75f),
            contentColor = Color.White
        )
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = duration.formattedDuration(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun Int.formattedDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return when {
        hours > 0 -> String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}