package com.galid.hemuser.controller

import com.galid.hemuser.service.TokenService
import com.galid.hemuser.service.dto.TokenDto
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tokens")
class TokenController(
    private val tokenService: TokenService
) {
    @PutMapping
    fun renewAuthToken(
        @RequestBody request: TokenDto.RenewAuthTokenRequest
    ): Response<TokenDto.RenewAuthTokenResponse> {
        return Response(
            data = TokenDto.RenewAuthTokenResponse(
                tokenService.renewAuthToken(request.refreshToken)
            )
        )
    }
}