package com.galid.hemuser.service

import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.service.dto.UserDto

interface LoginService {
    fun validateLoginInfo(
        request: UserDto.LoginRequest
    ): UserEntity
}