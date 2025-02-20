package edu.mirea.onebeattrue.vktest.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import edu.mirea.onebeattrue.vktest.di.DaggerApplicationComponent

class VkTestApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

val applicationComponent
    @Composable get() = (LocalContext.current.applicationContext as VkTestApplication).component
