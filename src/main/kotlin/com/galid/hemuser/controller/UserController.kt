package com.galid.hemuser.controller

import com.galid.hemuser.service.UserService
import com.galid.hemuser.service.dto.UserDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun join(
        @RequestBody request: UserDto.JoinRequest
    ): Response<UserDto.JoinResponse> {
        return Response(
            data = userService.join(request)
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: UserDto.LoginRequest
    ): Response<UserDto.LoginResponse> {
        return Response(
            data = userService.login(request)
        )
    }
}