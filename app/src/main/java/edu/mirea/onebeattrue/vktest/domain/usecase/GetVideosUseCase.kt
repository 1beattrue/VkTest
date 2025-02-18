package edu.mirea.onebeattrue.vktest.domain.usecase

import androidx.paging.PagingData
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository,
) {
    operator fun invoke(): Flow<PagingData<Video>> = repository.getVideos()
}
