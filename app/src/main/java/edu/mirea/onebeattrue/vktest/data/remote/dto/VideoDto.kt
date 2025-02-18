package edu.mirea.onebeattrue.vktest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("image") val image: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("user") val author: AuthorDto,
    @SerializedName("video_files") val videoFiles: List<VideoFileDto>,
    @SerializedName("video_pictures") val videoPictures: List<VideoPictureDto>
)