package edu.mirea.onebeattrue.vktest.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Video(
    val id: Long,
    val width: Int,
    val height: Int,
    val thumbnail: String,
    val duration: Int,
    val author: String,
    val videoFile: String,
    val previewPicture: String,
    val page: Int
)