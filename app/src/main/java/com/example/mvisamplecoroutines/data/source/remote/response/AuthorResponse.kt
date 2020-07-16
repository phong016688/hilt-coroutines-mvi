package com.example.mvisamplecoroutines.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("ID")
    val id: String,
    @SerializedName("IDBook")
    val idBook: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String
)