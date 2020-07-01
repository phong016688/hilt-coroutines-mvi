package com.example.mvisamplecoroutines.domain.entity

data class Book(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val pageCount: Int = 0,
    val excerpt: String = "",
    val date: String = ""
)