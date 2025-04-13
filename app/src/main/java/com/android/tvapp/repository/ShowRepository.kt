package com.android.tvapp.repository

import com.android.tvapp.data.Show
import com.android.tvapp.ui.TvMazeApi

class ShowRepository(private val api: TvMazeApi) {

    // In-memory cache for quick access to previously fetched shows
    private val cache = mutableMapOf<String, Show>()

    // Function to search for a show using the API
    suspend fun searchShow(query: String): Result<Show> {
        val key = query.lowercase()

        // Return cached result if available
        cache[key]?.let {
            return Result.success(it)
        }

        return try {
            val show = api.searchShow(query)
            cache[key] = show // Cache the result
            Result.success(show)
        } catch (e: Exception) {
            Result.failure(e) // Handle and wrap any exceptions
        }
    }
}
