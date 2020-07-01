package com.example.mvisamplecoroutines.data.source.service

import com.example.mvisamplecoroutines.data.source.response.BookResponse
import com.example.mvisamplecoroutines.data.source.response.AuthorResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface RestFullApi {
    @GET("Authors")
    suspend fun getAuthors(): List<AuthorResponse>

    @GET("Books")
    suspend fun getBooks(): List<BookResponse>
}