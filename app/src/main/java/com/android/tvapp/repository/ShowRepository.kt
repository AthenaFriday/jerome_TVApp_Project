package com.android.tvapp.repository

import android.util.Log
import com.android.tvapp.data.Show
import com.android.tvapp.ui.TvMazeApi

class ShowRepository(private val api: TvMazeApi) {

    // Cache the list of shows per query
    private val cache = mutableMapOf<String, List<Show>>()

    // Retrieve cached shows if available
    fun getCachedShows(query: String): List<Show>? {
        val key = query.lowercase()
        val cached = cache[key]
        if (cached != null) {
            Log.d("ShowRepository", "Using cached results for \"$query\"")
        }
        return cached
    }

    // Perform network search and cache results
    suspend fun searchShows(query: String): Result<List<Show>> {
        return try {
            // Fetch the list of search results from the API
            val results = api.searchShows(query)

            // Extract the actual Show data from the ShowSearchResult
            val shows = results.map { it.show }

            // Cache the results for the query
            cache[query.lowercase()] = shows

            Log.d("ShowRepository", "Fetched and cached results for \"$query\"")
            Result.success(shows) // Return the list of shows
        } catch (e: Exception) {
            // Handle any errors during the network request
            Log.e("ShowRepository", "Error searching for shows: ${e.message}")
            Result.failure(e) // Return the error as a failure result
        }
    }
}
