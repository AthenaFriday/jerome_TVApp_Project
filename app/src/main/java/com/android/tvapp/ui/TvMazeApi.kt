package com.android.tvapp.ui

import com.android.tvapp.data.ShowSearchResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeApi {
    // Fix: Correct the @GET URL and properly use the query parameter
    @GET("search/shows")
    suspend fun searchShows(@Query("q") query: String): List<ShowSearchResult> // Correct usage of @Query

    companion object {
        fun create(): TvMazeApi {
            return Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/") // Base URL for TVMaze API
                .addConverterFactory(GsonConverterFactory.create()) // Gson converter for JSON parsing
                .build()
                .create(TvMazeApi::class.java)
        }
    }
}
