package edu.mirea.onebeattrue.vktest.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.vktest.presentation.main.MainComponent
import edu.mirea.onebeattrue.vktest.presentation.video.VideoComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Main(val component: MainComponent) : Child
        data class Video(val component: VideoComponent) : Child
    }
}
