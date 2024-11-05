package com.davichois.ceis.presentation.event_management.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davichois.ceis.R
import com.davichois.ceis.databinding.FragmentHomeEventBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeEventFragment : Fragment(R.layout.fragment_home_event) {

    private var _binding: FragmentHomeEventBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeEventBinding.inflate(
            inflater,
            container,
            false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}