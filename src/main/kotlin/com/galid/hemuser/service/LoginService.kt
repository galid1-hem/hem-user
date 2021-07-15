package com.galid.hemuser.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val encoder: PasswordEncoder
) {
    fun validateLoginInfo(
        requestPassword: String,
        storedPassword: String
    ) {
        val encodedRequestPassword = encoder.encode(requestPassword)

        if (! encoder.matches(encodedRequestPassword, storedPassword))
            throw RuntimeException("비밀번호가 일치하지 않습니다.")
    }
}