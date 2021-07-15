package com.galid.hemuser.service.dto

class UserDto {
    data class JoinRequest(
        val accountId: String,
        val password: String,
        val email: String ?= null,
        val profileImageUrl: String ?= null,
        val nickName: String ?= null
    )

    data class JoinResponse(
        val id: Long,
    )

    data class LoginRequest(
        val accountId: String,
        val password: String
    )

    data class LoginResponse(
        val id: Long,
        val authToken: String,
        val refreshToken: String,
    )

    data class UserResponse(
        val id: Long,
        val userInformation: UserPrivateInformationDto
    )

    data class UserPrivateInformationDto(
        val profileImageUrl: String? = null,
        val nickName: String? = null,
        val email: String? = null
    )
}