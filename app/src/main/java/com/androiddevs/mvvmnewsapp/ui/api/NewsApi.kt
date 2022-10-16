package com.androiddevs.mvvmnewsapp.ui.api

import com.androiddevs.mvvmnewsapp.ui.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //get request to get only the breaking news by using entry point of the api that is v2/top-headlines
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode:String="us",
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=API_KEY
    ):Response<NewsResponse>


    //get request to get only the every news by using entry point of the api that is v2/everything
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery:String,
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=API_KEY
    ):Response<NewsResponse>
}