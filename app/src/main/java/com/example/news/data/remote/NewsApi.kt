package com.example.news.data.remote

import com.example.news.BuildConfig
import com.example.news.data.remote.response.News
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getCategoryList(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY ,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): News


    @GET("top-headlines")
    suspend fun getSearchList(
        @Query("country") country: String,
        @Query("q") q: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY): News

}
