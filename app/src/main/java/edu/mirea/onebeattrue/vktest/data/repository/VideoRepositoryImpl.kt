package edu.mirea.onebeattrue.vktest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import edu.mirea.onebeattrue.vktest.data.mapper.toEntity
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val pager: Pager<Int, VideoDbModel>,
) : VideoRepository {
    override fun getVideos(): Flow<PagingData<Video>> = pager.flow
        .map { pagingData -> pagingData.map { it.toEntity() } }
}