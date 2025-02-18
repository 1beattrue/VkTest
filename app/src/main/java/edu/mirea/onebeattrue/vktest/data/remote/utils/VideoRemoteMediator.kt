package edu.mirea.onebeattrue.vktest.data.remote.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import edu.mirea.onebeattrue.vktest.data.local.db.AppDatabase
import edu.mirea.onebeattrue.vktest.data.local.db.VideoDao
import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import edu.mirea.onebeattrue.vktest.data.mapper.toDbModels
import edu.mirea.onebeattrue.vktest.data.remote.api.VideoApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class VideoRemoteMediator(
    private val db: AppDatabase,
    private val dao: VideoDao,
    private val api: VideoApiService,
) : RemoteMediator<Int, VideoDbModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VideoDbModel>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    val first = state.firstItemOrNull()
                    if (first == null) {
                        return MediatorResult.Success(true)
                    } else {
                        first.page - first.perPage
                    }
                }

                LoadType.APPEND -> {
                    val last = state.lastItemOrNull()
                    if (last == null) {
                        return MediatorResult.Success(true)
                    } else {
                        last.page + last.perPage + 1
                    }
                }
            }

            val response = api.getVideos(
                page = page,
                pageCount = state.config.pageSize
            )

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }
                dao.upsertAll(response.toDbModels())
            }

            MediatorResult.Success(
                response.nextPage == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}