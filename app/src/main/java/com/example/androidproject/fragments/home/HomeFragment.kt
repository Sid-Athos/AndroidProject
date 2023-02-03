package com.example.androidproject.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.androidproject.R


class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar: EditText = view.findViewById(R.id.search_bar)

        searchBar.setOnFocusChangeListener { editView, b ->
            val bestsellerContainer:FragmentContainerView = view.findViewById(R.id.bestseller_container)
            val searchContainer:FragmentContainerView = view.findViewById(R.id.search_container)
            val searchFragment = childFragmentManager.findFragmentById(R.id.search_container) as Search

            bestsellerContainer.visibility = View.GONE
            searchContainer.visibility = View.VISIBLE
            searchBar.onFocusChangeListener = null
            searchBar.doOnTextChanged { text, start, before, count -> searchFragment.bindSearch(text.toString()) }
        }
    }
}