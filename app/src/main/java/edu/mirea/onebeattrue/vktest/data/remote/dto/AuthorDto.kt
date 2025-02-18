package edu.mirea.onebeattrue.vktest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("name") val name: String
)