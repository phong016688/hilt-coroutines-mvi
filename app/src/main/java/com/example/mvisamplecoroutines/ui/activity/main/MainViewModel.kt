package com.example.mvisamplecoroutines.ui.activity.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvisamplecoroutines.domain.repository.Repository

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
}