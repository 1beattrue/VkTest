package edu.mirea.onebeattrue.vktest.presentation.video

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktest.domain.model.Video

class DefaultVideoComponent @AssistedInject constructor(
    @Assisted("video") override val video: Video,
    @Assisted("componentContext") componentContext: ComponentContext,
) : VideoComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("video") video: Video,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultVideoComponent
    }
}