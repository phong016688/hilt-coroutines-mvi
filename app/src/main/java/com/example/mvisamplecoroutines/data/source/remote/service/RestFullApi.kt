package com.example.mvisamplecoroutines.data.source.remote.service

import com.example.mvisamplecoroutines.data.source.BaseResponse
import com.example.mvisamplecoroutines.data.source.remote.response.LoginResponse
import com.example.mvisamplecoroutines.data.source.remote.response.ProfileResponse
import retrofit2.http.*

interface RestFullApi {
    @POST("auth/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("event/delete-event")
    @FormUrlEncoded
    suspend fun deleteEvent(@Field("_id") id: String)

    @POST("event/complete-event")
    @FormUrlEncoded
    suspend fun completeEvent(@Field("_id") eventId: String)

    @POST("event/cancel-event")
    @FormUrlEncoded
    suspend fun cancelEvent(@Field("_id") eventId: String)

    @GET("user/profile")
    suspend fun getProfileCurrentUser(): BaseResponse<ProfileResponse>
}