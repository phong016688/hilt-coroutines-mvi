package com.example.mvisamplecoroutines.domain.repository

import com.example.mvisamplecoroutines.domain.entity.Author
import com.example.mvisamplecoroutines.domain.entity.Book

interface Repository {
    suspend fun getAuthors(): List<Author>
    suspend fun getBooks(): List<Book>
    suspend fun login(): String
}