package com.example.mvisamplecoroutines.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("ID")
    val id: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("PageCount")
    val pageCount: Int,
    @SerializedName("Excerpt")
    val excerpt: String,
    @SerializedName("PublishDate")
    val date: String
)