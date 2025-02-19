package edu.mirea.onebeattrue.vktest.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.vktest.data.local.db.AppDatabase
import edu.mirea.onebeattrue.vktest.data.local.db.VideoDao
import edu.mirea.onebeattrue.vktest.data.remote.api.ApiFactory
import edu.mirea.onebeattrue.vktest.data.remote.api.VideoApiService
import edu.mirea.onebeattrue.vktest.data.repository.VideoRepositoryImpl
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository

@Module
interface DataModule {

    @[Binds ApplicationScope]
    fun bindVideoRepository(impl: VideoRepositoryImpl): VideoRepository

    companion object {

        @[Provides ApplicationScope]
        fun provideApiService(): VideoApiService = ApiFactory.videoApiService

        @[Provides ApplicationScope]
        fun provideDatabase(
            context: Context,
        ): AppDatabase = AppDatabase.getInstance(context)

        @[Provides ApplicationScope]
        fun provideVideoDao(
            database: AppDatabase,
        ): VideoDao = database.videoDao()
    }
}