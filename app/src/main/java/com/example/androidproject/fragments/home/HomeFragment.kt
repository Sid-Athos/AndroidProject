package com.example.androidproject.fragments.home

import android.os.Bundle
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

        val bestsellerContainer:FragmentContainerView = view.findViewById(R.id.bestseller_container)
        val searchContainer:FragmentContainerView = view.findViewById(R.id.search_container)
        val searchFragment = childFragmentManager.findFragmentById(R.id.search_container) as Search

        var displayedSearch = false

        searchBar.doOnTextChanged { text, start, before, count ->
            run {
                if (text != null && text.isEmpty()) {
                    bestsellerContainer.visibility = View.VISIBLE
                    searchContainer.visibility = View.GONE
                    displayedSearch = false
                } else if (!displayedSearch) {
                    bestsellerContainer.visibility = View.GONE
                    searchContainer.visibility = View.VISIBLE
                    displayedSearch = true
                }

                searchFragment.bindSearch(text.toString())
            }
        }
    }
}