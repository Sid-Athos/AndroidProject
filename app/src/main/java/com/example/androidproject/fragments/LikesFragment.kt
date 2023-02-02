package com.example.androidproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R


class LikesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        initBackButton(view)
        return view
    }

    private fun initBackButton(view: View) {
        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val action: NavDirections = LikesFragmentDirections.actionLikesFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}