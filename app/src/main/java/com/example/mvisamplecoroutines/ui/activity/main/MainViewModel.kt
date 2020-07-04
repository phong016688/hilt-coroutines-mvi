package com.example.mvisamplecoroutines.ui.activity.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvisamplecoroutines.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        runBlocking(Dispatchers.IO) {
            repository.getAuthors()
        }
    }
}