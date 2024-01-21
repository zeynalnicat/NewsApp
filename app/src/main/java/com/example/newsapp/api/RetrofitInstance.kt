package com.example.newsapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val url = "https://newsapi.org/v2/"

object RetrofitInstance {
    private var INSTANCE: Retrofit? = null


    fun getInstance(): Retrofit? {
        if (INSTANCE == null) {
            val okkHttpClient = OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor())
                .build()

            INSTANCE = Retrofit.Builder().baseUrl(url).client(okkHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()

        }
        return INSTANCE
    }
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${Const.apiKey}")
            .build()

        return chain.proceed(request)

    }

}