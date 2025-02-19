package edu.mirea.onebeattrue.vktest.data.repository

import androidx.room.withTransaction
import edu.mirea.onebeattrue.vktest.data.local.db.AppDatabase
import edu.mirea.onebeattrue.vktest.data.local.db.VideoDao
import edu.mirea.onebeattrue.vktest.data.mapper.toDbModels
import edu.mirea.onebeattrue.vktest.data.mapper.toEntities
import edu.mirea.onebeattrue.vktest.data.mapper.toEntity
import edu.mirea.onebeattrue.vktest.data.remote.api.VideoApiService
import edu.mirea.onebeattrue.vktest.domain.model.Video
import edu.mirea.onebeattrue.vktest.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val api: VideoApiService,
    private val dao: VideoDao,
) : VideoRepository {

    override fun getVideos(): Flow<List<Video>> = dao.getVideos().map { it.toEntities() }

    override suspend fun getVideoById(id: Long): Video = withContext(Dispatchers.IO) {
        dao.getVideoById(id).toEntity()
    }

    override suspend fun loadNext(page: Int) = fetchAndCacheVideos(page)

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        db.withTransaction {
            dao.clearAll()
            fetchAndCacheVideos(FIRST_PAGE)
        }
    }

    private suspend fun fetchAndCacheVideos(page: Int) {
        val videos = api.getVideos(page).videos.toDbModels(page)
        dao.upsertAll(videos)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
