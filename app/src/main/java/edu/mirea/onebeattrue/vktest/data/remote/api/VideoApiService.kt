package edu.mirea.onebeattrue.vktest.data.remote.api

import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {

    @GET("videos/popular")
    suspend fun getVideos(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
    ): VideoResponseDto
}