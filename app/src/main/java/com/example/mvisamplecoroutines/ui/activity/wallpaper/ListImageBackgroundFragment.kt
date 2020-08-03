package com.example.mvisamplecoroutines.ui.activity.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvisamplecoroutines.databinding.FragmentListImageBackgroundBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListImageBackgroundFragment : Fragment() {
    private lateinit var binding: FragmentListImageBackgroundBinding
    private lateinit var adapter: ImageBackgroundAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListImageBackgroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        adapter = ImageBackgroundAdapter()
        binding.imageBackgroundRecyclerView.layoutManager = manager
        binding.imageBackgroundRecyclerView.adapter = adapter
        binding.imageBackgroundRecyclerView.hasFixedSize()
        adapter.submitList((1..1000).toList())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ListImageBackgroundFragment()
    }
}