package com.example.mvisamplecoroutines.data.repository

import com.example.mvisamplecoroutines.data.source.remote.service.RestFullApi
import com.example.mvisamplecoroutines.domain.entity.User
import com.example.mvisamplecoroutines.domain.repository.Repository
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: RestFullApi
) : Repository {

    override suspend fun login(email: String, password: String): User {
        delay(1000)
        return User()
    }
}