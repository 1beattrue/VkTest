package edu.mirea.onebeattrue.vktest.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel

@Dao
interface VideoDao {
    @Upsert
    suspend fun upsertAll(videos: List<VideoDbModel>)

    @Query("SELECT * FROM video")
    fun pagingSource(): PagingSource<Int, VideoDbModel>

    @Query("DELETE FROM video")
    suspend fun clearAll()
}