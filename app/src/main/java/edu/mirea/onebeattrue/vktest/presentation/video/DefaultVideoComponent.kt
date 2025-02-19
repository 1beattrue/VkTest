package edu.mirea.onebeattrue.vktest.presentation.video

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultVideoComponent @AssistedInject constructor(
    @Assisted("videoId") private val videoId: Long,
    @Assisted("componentContext") componentContext: ComponentContext,
) : VideoComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("videoId") videoId: Long,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultVideoComponent
    }
}