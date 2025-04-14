package com.android.tvapp.repository

import android.util.Log
import com.android.tvapp.data.Show
import com.android.tvapp.ui.TvMazeApi

class   ShowRepository(private val api: TvMazeApi) {

    // In-memory cache for quick access to previously fetched shows
    private val cache = mutableMapOf<String, Show>()

    fun getCachedShow(query: String): Show? {
        val key = query.lowercase()
        val cached = cache[key]
        if (cached != null) {
            Log.d("ShowRepository", "Using cached version for \"$query\"")
        }
        return cached
    }

    suspend fun searchShow(query: String): Result<Show> {
        return try {
            val show = api.searchShow(query)
            cache[query.lowercase()] = show // Cache the result
            Log.d("ShowRepository", "Fetched and cached \"$query\" from API")
            Result.success(show)
        } catch (e: Exception) {
            Log.e("ShowRepository", "Failed to fetch \"$query\": ${e.message}")
            Result.failure(e)
        }
    }
}
