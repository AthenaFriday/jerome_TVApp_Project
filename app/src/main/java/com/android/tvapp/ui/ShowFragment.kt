package com.android.tvapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.android.tvapp.R
import com.android.tvapp.data.Show

class ShowFragment : Fragment() {

    companion object {
        private const val SHOW_KEY = "show_key"

        fun newInstance(shows: List<Show>?): ShowFragment {
            val fragment = ShowFragment()
            val bundle = Bundle()
            // Store the list of shows as Serializable
            bundle.putSerializable(SHOW_KEY, ArrayList(shows)) // Use ArrayList for List<Show>
            fragment.arguments = bundle
            return fragment
        }
    }

    private var shows: List<Show>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the list of shows from arguments
        shows = arguments?.getSerializable(SHOW_KEY) as? List<Show>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showImageView = view.findViewById<ImageView>(R.id.showImageView)
        val nameTextView = view.findViewById<TextView>(R.id.showNameTextView)
        val premiereTextView = view.findViewById<TextView>(R.id.premiereDaysTextView)

        shows?.forEach { show ->
            nameTextView.text = show.name ?: "No Title"
            premiereTextView.text = "Premiered: ${show.premiered ?: "Unknown"}"

            // Load image safely with Coil
            val imageUrl = show.image?.medium
            if (!imageUrl.isNullOrEmpty()) {
                showImageView.load(imageUrl) {
                    crossfade(true)
                }
            }
        }
    }
}
