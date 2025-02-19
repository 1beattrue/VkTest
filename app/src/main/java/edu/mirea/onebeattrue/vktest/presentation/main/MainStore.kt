package edu.mirea.onebeattrue.vktest.presentation.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.Intent
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.Label
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.State
import javax.inject.Inject


interface MainStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ClickVideo(val videoId: Long) : Intent
    }

    data class State(val videos: List<Video>)

    sealed interface Label {
        data class OnVideoClicked(val videoId: Long) : Label
    }
}

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = MainStore::class.simpleName,
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ClickVideo -> publish(Label.OnVideoClicked(intent.videoId))
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {

                else -> copy()
            }
    }
}
