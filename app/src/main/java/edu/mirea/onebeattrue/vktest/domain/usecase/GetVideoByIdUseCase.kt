package edu.mirea.onebeattrue.vktest.domain.usecase

import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import javax.inject.Inject

data class GetVideoByIdUseCase @Inject constructor(
    private val repository: VideoRepository,
) {
    suspend operator fun invoke(id: Long): Video = repository.getVideoById(id)
}
