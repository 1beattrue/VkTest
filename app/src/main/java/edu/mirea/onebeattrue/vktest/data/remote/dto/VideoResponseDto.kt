package edu.mirea.onebeattrue.vktest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("prev_page") val prevPage: String?,
    @SerializedName("next_page") val nextPage: String?,
    @SerializedName("videos") val videos: List<VideoDto>,
)