package edu.mirea.onebeattrue.vktest.domain.usecase

import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository,
) {
    operator fun invoke(): Flow<List<Video>> = repository.getVideos()
}
