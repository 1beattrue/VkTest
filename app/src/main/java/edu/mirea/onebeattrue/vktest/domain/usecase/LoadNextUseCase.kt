package edu.mirea.onebeattrue.vktest.domain.usecase

import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import javax.inject.Inject

data class LoadNextUseCase @Inject constructor(
    private val repository: VideoRepository,
) {
    suspend operator fun invoke(page: Int) = repository.loadNext(page)
}
