package com.example.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.suweorajamuapps.R
import dagger.hilt.android.AndroidEntryPoint
import android.widget.TextView

@AndroidEntryPoint
class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext()).apply {
            text = "Aplikasi Warung Jamu Suwe Ora Jamu \n" +
                    "Created By : Louis Jonathan Susanto Putra \n" +
                    "Email : jonathanlouis568@gmail.com"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
    }

}