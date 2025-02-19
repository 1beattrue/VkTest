package edu.mirea.onebeattrue.vktest.presentation.main

import kotlinx.coroutines.flow.StateFlow

interface MainComponent {
    val model: StateFlow<MainStore.State>

    fun clickVideo(videoId: Long)
}