package edu.mirea.onebeattrue.vktest.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video")
data class VideoDbModel(
    @PrimaryKey
    val id: Long,
    val thumbnail: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val author: String,
    val videoUrl: String,
    val previewPicture: String,
    val page: Int
)