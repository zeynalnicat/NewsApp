package com.example.newsapp.api

import com.example.newsapp.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

   @GET("top-headlines")
   suspend fun getTop(@Query("country") country:String):Response<News>



   @GET("everything")
   suspend fun search(@Query("q") query:String):Response<News>


}