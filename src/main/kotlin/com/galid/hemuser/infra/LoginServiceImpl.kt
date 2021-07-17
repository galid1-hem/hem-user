package com.galid.hemuser.infra

import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.service.LoginService
import com.galid.hemuser.service.dto.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val encoder: PasswordEncoder,
    private val userRepository: UserRepository
): LoginService {
    override fun validateLoginInfo(
        loginRequest: UserDto.LoginRequest
    ): UserEntity {
        val foundUser = userRepository.findByUserAccount_AccountId(loginRequest.accountId)
            .orElseThrow { throw RuntimeException("존재하지 않는 ID입니다.") }

        val encodedRequestPassword = encoder.encode(loginRequest.password)

        if (! encoder.matches(encodedRequestPassword, foundUser.userAccount.password))
            throw RuntimeException("비밀번호가 일치하지 않습니다.")

        return foundUser
    }
}