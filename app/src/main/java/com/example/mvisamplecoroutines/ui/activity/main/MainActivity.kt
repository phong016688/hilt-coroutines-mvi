package com.example.mvisamplecoroutines.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.ActivityMainBinding
import com.example.mvisamplecoroutines.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBindings(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        handleEvent()
    }

    private fun handleEvent() {
        val nav = findNavController(R.navigation.nav_graph)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
