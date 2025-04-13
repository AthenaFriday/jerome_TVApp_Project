package com.android.tvapp.model

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
        viewModelScope.launch {
            uiState = UiState.Loading
            val result = repository.searchShow(query)
            uiState = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error("Show not found.") }
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
