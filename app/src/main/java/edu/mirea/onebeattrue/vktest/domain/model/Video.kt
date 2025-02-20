package edu.mirea.onebeattrue.vktest.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Video(
    val id: Long,
    val width: Int,
    val height: Int,
    val thumbnail: String,
    val duration: Int,
    val author: String,
    val videoUrl: String,
    val previewPicture: String,
    val page: Int,
)