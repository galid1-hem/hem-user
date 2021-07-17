package com.galid.hemuser.controller

import com.galid.hemuser.service.dto.UserDto
import com.galid.hemuser.service.usecase.UserUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userUse: UserUseCase
) {
    @PostMapping
    fun join(
        @RequestBody request: UserDto.JoinRequest
    ): Response<UserDto.JoinResponse> {
        return Response(
            data = userUse.join(request)
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: UserDto.LoginRequest
    ): Response<UserDto.LoginResponse> {
        return Response(
            data = userUse.login(request)
        )
    }
}