package com.galid.hemuser.service.dto

class TokenDto {
    data class RenewAuthTokenRequest(
        val refreshToken: String,
    )

    data class RenewAuthTokenResponse(
        val authToken: String
    )
}