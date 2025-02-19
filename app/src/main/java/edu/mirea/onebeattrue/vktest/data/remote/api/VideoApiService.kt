package edu.mirea.onebeattrue.vktest.data.remote.api

import edu.mirea.onebeattrue.vktest.data.remote.dto.VideoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {

    @GET("videos/popular")
    suspend fun getVideos(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): VideoResponseDto

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
    }
}