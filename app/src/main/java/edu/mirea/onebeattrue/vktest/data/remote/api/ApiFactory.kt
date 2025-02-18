package edu.mirea.onebeattrue.vktest.data.remote.api

import edu.mirea.onebeattrue.vktest.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {
    private const val BASE_URL = "https://api.pexels.com/"
    private const val HEADER_KEY = "Authorization"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()


            val newRequest = originalRequest
                .newBuilder()
                .addHeader(HEADER_KEY, BuildConfig.API_KEY)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val videoApiService: VideoApiService = retrofit.create()
}