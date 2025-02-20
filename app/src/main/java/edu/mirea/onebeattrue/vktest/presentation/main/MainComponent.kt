package edu.mirea.onebeattrue.vktest.presentation.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {
    val model: StateFlow<MainStore.State>
    val labels: Flow<MainStore.Label>
    fun clickVideo(videoId: Long)
    fun loadNext()
    fun refresh()
}