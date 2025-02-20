package edu.mirea.onebeattrue.vktest.presentation.main

import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {
    val model: StateFlow<MainStore.State>
    val labels: Flow<MainStore.Label>
    fun clickVideo(video: Video)
    fun loadNext()
    fun refresh()
}