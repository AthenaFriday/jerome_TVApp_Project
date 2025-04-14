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

    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    fun searchShow(query: String) {
        val trimmedQuery = query.trim()

        // Check cache first
        val cached = repository.getCachedShow(trimmedQuery)
        if (cached != null) {
            Log.d("ShowViewModel", "Showing cached result for \"$trimmedQuery\"")
            uiState = UiState.Success(cached)
            return
        }

        // Otherwise, fetch from API
        viewModelScope.launch {
            Log.d("ShowViewModel", "Fetching from network for \"$trimmedQuery\"")
            uiState = UiState.Loading
            val result = repository.searchShow(trimmedQuery)
            uiState = result.fold(
                onSuccess = {
                    Log.d("ShowViewModel", "Successfully fetched \"$trimmedQuery\"")
                    UiState.Success(it)
                },
                onFailure = {
                    Log.e("ShowViewModel", "Error fetching \"$trimmedQuery\"")
                    UiState.Error("Show not found.")
                }
            )
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val show: Show) : UiState()
        data class Error(val message: String) : UiState()
    }
}
