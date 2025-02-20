package edu.mirea.onebeattrue.vktest.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.presentation.main.DefaultMainComponent
import edu.mirea.onebeattrue.vktest.presentation.video.DefaultVideoComponent
import kotlinx.serialization.Serializable


class DefaultRootComponent @AssistedInject constructor(
    private val mainComponentFactory: DefaultMainComponent.Factory,
    private val videoComponentFactory: DefaultVideoComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.MainConfig,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        is Config.VideoConfig -> {
            val component = videoComponentFactory.create(
                video = config.video,
                componentContext = componentContext,
            )
            RootComponent.Child.Video(component)
        }

        is Config.MainConfig -> {
            val component = mainComponentFactory.create(
                onVideoClicked = { video ->
                    navigation.pushNew(Config.VideoConfig(video))
                },
                componentContext = componentContext
            )
            RootComponent.Child.Main(component)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object MainConfig : Config

        @Serializable
        data class VideoConfig(val video: Video) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}