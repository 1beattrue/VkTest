package edu.mirea.onebeattrue.vktest.presentation.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.vktest.presentation.main.MainContent
import edu.mirea.onebeattrue.vktest.presentation.video.VideoContent

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Video -> {
                VideoContent(component = instance.component)
            }

            is RootComponent.Child.Main -> {
                MainContent(component = instance.component)
            }

        }
    }
}
