package com.example.mvisamplecoroutines.data.repository

import com.example.mvisamplecoroutines.data.source.remote.service.RestFullApi
import com.example.mvisamplecoroutines.domain.entity.User
import com.example.mvisamplecoroutines.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: RestFullApi
) : Repository {

    override suspend fun login(email: String, password: String): User {
        api.login(email, password).token
        return User()
    }
}