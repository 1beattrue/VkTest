package edu.mirea.onebeattrue.vktest.di

import android.content.Context
import androidx.media3.common.Player
import dagger.BindsInstance
import dagger.Component
import edu.mirea.onebeattrue.vktest.presentation.MainActivity

@ApplicationScope
@Component(
    modules = [DataModule::class, PresentationModule::class]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun getPlayer(): Player

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}