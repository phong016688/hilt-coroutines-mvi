package com.example.mvisamplecoroutines.data.source.remote.response

import com.example.mvisamplecoroutines.domain.entity.User
import com.google.gson.annotations.SerializedName
data class ProfileResponse(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val email: String,
    val avatar: String,
    val idGroup: String,
    val phoneNumber: String,
    val birthday: String,
    val isActive: Boolean,
    val isLocked: Boolean,
    val role: String,
    val createdAt: Double,
    val createdBy: User,
    val updatedAt: Double
) {
    fun toUser(): User {
        return User(
            id = this.id,
            name = this.name,
            email = this.email,
            birthday = this.birthday,
            phoneNumber = this.phoneNumber,
            avatar = this.avatar,
            createdAt = this.createdAt,
            createdBy = this.createdBy,
            isActive = this.isActive,
            isLocked = this.isLocked,
            groupId = this.idGroup,
            role = this.role,
            updatedAt = this.updatedAt
        )
    }
}