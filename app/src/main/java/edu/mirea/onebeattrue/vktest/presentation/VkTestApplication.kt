package edu.mirea.onebeattrue.vktest.presentation

import android.app.Application
import edu.mirea.onebeattrue.vktest.di.DaggerApplicationComponent

class VkTestApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}