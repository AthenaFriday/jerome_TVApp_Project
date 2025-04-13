package com.android.tvapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // ✅ Make sure Coil is imported
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSearchScreen(viewModel: ShowViewModel) {
    var query by remember { mutableStateOf("") }
    val state = viewModel.uiState

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search TV Show") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.searchShow(query) }) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ShowViewModel.UiState.Loading -> CircularProgressIndicator()
            is ShowViewModel.UiState.Success -> {
                val show = state.show
                show.image?.medium?.let { imageUrl: String ->
                    AsyncImage(model = imageUrl, contentDescription = show.name)
                }
                Text("Name: ${show.name}")
                val daysSince = show.premiered?.let { premiered ->
                    val date = LocalDate.parse(premiered)
                    ChronoUnit.DAYS.between(date, LocalDate.now())
                } ?: "N/A"
                Text("Days since premiere: $daysSince")
            }
            is ShowViewModel.UiState.Error -> Text(state.message, color = Color.Red)
            else -> {}
        }
    }
}
