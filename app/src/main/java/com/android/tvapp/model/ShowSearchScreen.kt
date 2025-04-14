package com.android.tvapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment          // ✅ FIXED IMPORT
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.tvapp.data.Show
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSearchScreen(viewModel: ShowViewModel) {
    var query by remember { mutableStateOf("") }
    val state = viewModel.uiState

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search TV Show") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (query.isNotBlank()) {
                    viewModel.searchShows(query)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ShowViewModel.UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ShowViewModel.UiState.Success -> {
                if (state.shows.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.shows) { show ->
                            ShowCard(show = show)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No results found.", color = Color.Gray)
                    }
                }
            }
            is ShowViewModel.UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = Color.Red)
                }
            }
            else -> {}
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowCard(show: Show) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        show.image?.medium?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = show.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp)
            )
        }

        Text(
            text = show.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val daysSince = show.premiered?.let {
            val premiereDate = LocalDate.parse(it)
            ChronoUnit.DAYS.between(premiereDate, LocalDate.now())
        } ?: "N/A"

        Text(
            text = "Days since premiere: $daysSince",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
