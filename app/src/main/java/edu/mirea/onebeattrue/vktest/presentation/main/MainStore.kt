package edu.mirea.onebeattrue.vktest.presentation.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.usecase.GetVideosUseCase
import edu.mirea.onebeattrue.vktest.domain.usecase.LoadNextUseCase
import edu.mirea.onebeattrue.vktest.domain.usecase.RefreshUseCase
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.Intent
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.Label
import edu.mirea.onebeattrue.vktest.presentation.main.MainStore.State
import edu.mirea.onebeattrue.vktest.presentation.utils.SafeCoroutineBootstrapper
import edu.mirea.onebeattrue.vktest.presentation.utils.SafeCoroutineExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


interface MainStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ClickVideo(val videoId: Long) : Intent
        data object Refresh : Intent
        data object LoadNext : Intent
    }

    data class State(
        val videos: List<Video>,
        val loadingState: LoadingState,
    ) {
        interface LoadingState {
            data object Initial : LoadingState
            data object NeedLoadNext : LoadingState
            data object Loading : LoadingState
            data object Error : LoadingState
            data object End : LoadingState
            data object Refreshing : LoadingState
        }
    }

    sealed interface Label {
        data class OnVideoClicked(val videoId: Long) : Label
        data class ShowError(val error: Throwable) : Label
    }
}

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getVideosUseCase: GetVideosUseCase,
    private val refreshUseCase: RefreshUseCase,
    private val loadNextUseCase: LoadNextUseCase,
) {

    fun create(): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = MainStore::class.simpleName,
            initialState = State(
                videos = emptyList(),
                loadingState = State.LoadingState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdateVideos(val videos: List<Video>) : Action
    }

    private sealed interface Msg {
        data class OnVideosUpdated(
            val videos: List<Video>,
            val loadingState: State.LoadingState,
        ) : Msg

        data class Loading(
            val loadingState: State.LoadingState,
        ) : Msg
    }

    private inner class BootstrapperImpl : SafeCoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getVideosUseCase()
                    .collectLatest { videos ->
                        dispatch(Action.UpdateVideos(videos))
                    }
            }
        }
    }

    private inner class ExecutorImpl : SafeCoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var loadNextJob: Job? = null

        override fun handleException(throwable: Throwable) {
            dispatch(Msg.Loading(State.LoadingState.Error))
            publish(Label.ShowError(throwable))
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ClickVideo -> publish(Label.OnVideoClicked(intent.videoId))


                Intent.LoadNext -> {
                    loadNextJob?.cancel()
                    loadNextJob = scope.launch {
                        dispatch(Msg.Loading(State.LoadingState.Loading))
                        delay(500)
                        loadNextUseCase()
                    }
                }

                Intent.Refresh -> {
                    scope.launch {
                        dispatch(Msg.Loading(State.LoadingState.Refreshing))
                        delay(500)
                        refreshUseCase()
                    }
                }

            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.UpdateVideos -> {
                    val videos = action.videos
                    val loadingState = if (videos.size < MAX_VIDEOS_COUNT) {
                        State.LoadingState.NeedLoadNext
                    } else {
                        State.LoadingState.End
                    }
                    dispatch(
                        Msg.OnVideosUpdated(
                            videos,
                            loadingState
                        )
                    )
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.OnVideosUpdated -> copy(
                    videos = msg.videos,
                    loadingState = msg.loadingState,
                )

                is Msg.Loading -> copy(loadingState = msg.loadingState)
            }
    }

    companion object {
        private const val MAX_VIDEOS_COUNT = 1000
    }
}
