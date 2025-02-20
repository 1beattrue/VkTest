package edu.mirea.onebeattrue.vktest.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    companion object {
        @Provides
        fun providePlayer(context: Context): Player = ExoPlayer.Builder(context).build()

        @Provides
        fun provideStoreFactory(): StoreFactory = LoggingStoreFactory(DefaultStoreFactory())
    }
}