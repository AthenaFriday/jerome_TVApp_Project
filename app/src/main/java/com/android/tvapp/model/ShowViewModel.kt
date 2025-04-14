package com.android.tvapp.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvapp.data.Show
import com.android.tvapp.repository.ShowRepository
import kotlinx.coroutines.launch

class ShowViewModel(private val repository: ShowRepository) : ViewModel() {

    // UI state to manage different view states (Idle, Loading, Success, Error)
    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    // Function to search for shows based on the user's query
    fun searchShows(query: String) {
        val cleanedQuery = query.trim() // Remove any extra spaces from the query
        val cached = repository.getCachedShows(cleanedQuery)

        // If shows are already cached, return them immediately
        if (cached != null) {
            Log.d("ShowViewModel", "Returning cached results for \"$cleanedQuery\"")
            uiState = UiState.Success(cached) // Update the UI with the cached results
            return
        }

        // If not cached, make a network request to fetch shows
        viewModelScope.launch {
            uiState = UiState.Loading // Show loading spinner
            val result = repository.searchShows(cleanedQuery)
            uiState = result.fold(
                onSuccess = { shows ->
                    if (shows.isNotEmpty()) {
                        UiState.Success(shows) // Update UI with the list of shows
                    } else {
                        UiState.Error("No TV shows found for \"$cleanedQuery\".")
                    }
                },
                onFailure = {
                    UiState.Error("Failed to load TV shows. Please try again later.")
                }
            )
        }
    }

    // Sealed class to represent different UI states
    sealed class UiState {
        data object Idle : UiState() // Initial state when nothing has happened
        data object Loading : UiState() // State when the request is being made
        data class Success(val shows: List<Show>) : UiState() // State when shows are found
        data class Error(val message: String) : UiState() // State when there's an error
    }
}
