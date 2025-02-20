package edu.mirea.onebeattrue.vktest.presentation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.Intent
import edu.mirea.onebeattrue.vktest.presentation.utils.scope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMainComponent @AssistedInject constructor(
    private val storeFactory: MainStoreFactory,
    @Assisted("onVideoClicked") onVideoClicked: (Long) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : MainComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is MainStore.Label.OnVideoClicked -> {
                        onVideoClicked(label.videoId)
                    }

                    else -> {}
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MainStore.State>
        get() = store.stateFlow

    override val labels: Flow<MainStore.Label>
        get() = store.labels

    override fun clickVideo(videoId: Long) {
        store.accept(Intent.ClickVideo(videoId))
    }

    override fun loadNext() {
        store.accept(Intent.LoadNext)
    }

    override fun refresh() {
        store.accept(Intent.Refresh)
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onVideoClicked") onVideoClicked: (Long) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultMainComponent
    }
}