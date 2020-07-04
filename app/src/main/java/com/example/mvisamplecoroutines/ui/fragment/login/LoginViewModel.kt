package com.example.mvisamplecoroutines.ui.fragment.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvisamplecoroutines.domain.repository.Repository


class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

}