package com.android.tvapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.tvapp.repository.ShowRepository
import com.android.tvapp.ShowFragment
import com.android.tvapp.ui.TvMazeApi
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var repository: ShowRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = ShowRepository(TvMazeApi.create())

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        // ✅ Properly handle back press (for API 34+ compatibility)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchShow(query)
            }
        }
    }

    private fun searchShow(query: String) {
        lifecycleScope.launch {
            val result = repository.searchShow(query.lowercase())

            supportFragmentManager.beginTransaction()
                .replace(R.id.resultContainer, ShowFragment.newInstance(result.getOrNull()))
                .addToBackStack(null) // 👈 optional: allow going back
                .commit()
        }
    }
}
