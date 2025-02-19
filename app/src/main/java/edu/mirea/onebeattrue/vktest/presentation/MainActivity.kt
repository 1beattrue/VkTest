package edu.mirea.onebeattrue.vktest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import edu.mirea.onebeattrue.vktest.presentation.root.DefaultRootComponent
import edu.mirea.onebeattrue.vktest.presentation.root.RootContent
import edu.mirea.onebeattrue.vktest.ui.theme.VkTestTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as VkTestApplication).component.inject(this)
        super.onCreate(savedInstanceState)
        val component = rootComponentFactory.create(defaultComponentContext())
        enableEdgeToEdge()
        setContent {
            VkTestTheme {
                RootContent(component)
            }
        }
    }
}