package edu.mirea.onebeattrue.vktest.domain.usecase

import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import javax.inject.Inject

data class RefreshUseCase @Inject constructor(
    private val repository: VideoRepository,
) {
    suspend operator fun invoke() = repository.refresh()
}
