package edu.mirea.onebeattrue.vktest.domain.repository

import androidx.paging.PagingData
import edu.mirea.onebeattrue.vktest.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideos(): Flow<PagingData<Video>>
}