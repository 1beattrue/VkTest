package edu.mirea.onebeattrue.vktest.domain.repository

import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideos(): Flow<List<Video>>
    suspend fun getVideoById(id: Long): Video
    suspend fun loadNext()
    suspend fun refresh()
}