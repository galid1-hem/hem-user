package com.galid.hemuser.controller

import com.galid.hemuser.infra.TokenServiceImpl
import com.galid.hemuser.service.dto.TokenDto
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tokens")
class TokenController(
    private val tokenServiceImpl: TokenServiceImpl
) {
    @PutMapping
    fun renewAuthToken(
        @RequestBody request: TokenDto.RenewAuthTokenRequest
    ): Response<TokenDto.RenewAuthTokenResponse> {
        return Response(
            data = TokenDto.RenewAuthTokenResponse(
                tokenServiceImpl.renewAuthToken(request.refreshToken)
            )
        )
    }
}