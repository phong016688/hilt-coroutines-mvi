package com.example.mvisamplecoroutines.data.repository

import com.example.mvisamplecoroutines.data.source.service.RestFullApi
import com.example.mvisamplecoroutines.domain.entity.Author
import com.example.mvisamplecoroutines.domain.entity.Book
import com.example.mvisamplecoroutines.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.delay
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: RestFullApi
) : Repository {
    override suspend fun getAuthors(): List<Author> {
        return api.getAuthors().map {
            Author(
                it.id,
                it.idBook,
                it.firstName,
                it.lastName
            )
        }
    }

    override suspend fun getBooks(): List<Book> {
        return api.getBooks().map {
            Book(
                it.id,
                it.title,
                it.description,
                it.pageCount,
                it.excerpt,
                it.date
            )
        }
    }

    override suspend fun login(email: String, password: String): String {
        delay(2_000)
        return "user12345"
    }
}