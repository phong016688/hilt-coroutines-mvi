package com.example.mvisamplecoroutines.domain.repository

import com.example.mvisamplecoroutines.domain.entity.User

interface Repository {
    suspend fun login(email: String, password: String): User
}