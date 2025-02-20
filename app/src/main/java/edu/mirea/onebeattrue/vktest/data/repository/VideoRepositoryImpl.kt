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

    override suspend fun loadNext() = withContext(Dispatchers.IO) {
        val lastPage = dao.getLastPage() ?: FIRST_PAGE
        val nextPage = lastPage + 1
        val videos = api.getVideos(nextPage, PAGE_COUNT).videos.toDbModels(nextPage)
        dao.upsertAll(videos)
    }

    override suspend fun refresh() {
        val videos = api.getVideos(FIRST_PAGE, PAGE_COUNT).videos.toDbModels(FIRST_PAGE)
        db.withTransaction {
            dao.clearAll()
            dao.upsertAll(videos)
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val PAGE_COUNT = 15
    }
}
