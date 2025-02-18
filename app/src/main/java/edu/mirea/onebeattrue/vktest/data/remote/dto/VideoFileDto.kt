package edu.mirea.onebeattrue.vktest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoFileDto(
    @SerializedName("quality") val quality: String,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("link") val link: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)