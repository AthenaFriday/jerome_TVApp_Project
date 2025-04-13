package com.android.tvapp.ui

import com.android.tvapp.data.Show
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeApi {
    @GET("singlesearch/shows")
    suspend fun searchShow(@Query("q") query: String): Show

    companion object {
        fun create(): TvMazeApi {
            return Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TvMazeApi::class.java)
        }
    }
}
