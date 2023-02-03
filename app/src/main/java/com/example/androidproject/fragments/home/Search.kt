package com.example.androidproject.fragments.home

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidproject.R

class Search: Fragment(R.layout.fragment_search) {
    fun bindSearch(input: String) {
        requireView().findViewById<TextView>(R.id.title).text = input
    }
}