package com.example.mvisamplecoroutines.data.source

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("code")
    val code: String? = null
)