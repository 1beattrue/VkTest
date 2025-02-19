package edu.mirea.onebeattrue.vktest.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Upsert
    suspend fun upsertAll(videos: List<VideoDbModel>)

    @Query("SELECT * FROM video")
    fun getVideos(): Flow<List<VideoDbModel>>

    @Query("SELECT * FROM video WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: Long): VideoDbModel

    @Query("DELETE FROM video")
    suspend fun clearAll()
}