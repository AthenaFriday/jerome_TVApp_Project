package com.android.tvapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.android.tvapp.model.ShowSearchScreen
import com.android.tvapp.model.ShowViewModel
import com.android.tvapp.repository.ShowRepository
import com.android.tvapp.ui.TvMazeApi
import com.android.tvapp.ui.theme.TvAppTheme // If you have one

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate repository and ViewModel
        val repository = ShowRepository(TvMazeApi.create())
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ShowViewModel(repository) as T
            }
        })[ShowViewModel::class.java]

        setContent {
            TvAppTheme { // You can remove this line if you don't use a custom theme
                ShowSearchScreen(viewModel = viewModel)
            }
        }
    }
}
