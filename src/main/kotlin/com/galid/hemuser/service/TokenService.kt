package com.galid.hemuser.service

interface TokenService {
    fun createRefreshToken(userId: Long): String
    fun renewAuthToken(refreshToken: String): String
}