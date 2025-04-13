package com.android.tvapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.android.tvapp.data.Show

class ShowFragment : Fragment() {

    companion object {
        private const val SHOW_KEY = "show_key"

        fun newInstance(show: Show?): ShowFragment {
            val fragment = ShowFragment()
            val bundle = Bundle()
            bundle.putSerializable(SHOW_KEY, show)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var show: Show? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        show = arguments?.getSerializable(SHOW_KEY) as? Show
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

        show?.let {
            nameTextView.text = it.name ?: "No Title"
            premiereTextView.text = "Premiered: ${it.premiered ?: "Unknown"}"

            // Load image safely with Coil
            val imageUrl = it.image?.medium
            if (!imageUrl.isNullOrEmpty()) {
                showImageView.load(imageUrl) {
                    crossfade(true)
                }
            }
        }
    }
}
