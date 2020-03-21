package com.gulshansutey.mygithub.networking

import com.gulshansutey.mygithub.model.GitRepoServiceResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubSearchService {
    @GET("search/repositories")
    fun searchRepos(
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<GitRepoServiceResponse>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubSearchService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubSearchService::class.java)
        }
    }
}