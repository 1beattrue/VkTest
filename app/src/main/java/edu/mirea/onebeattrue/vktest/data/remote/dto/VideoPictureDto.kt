package edu.mirea.onebeattrue.vktest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoPictureDto(
    @SerializedName("picture") val picture: String,
)