package com.galid.hemuser.service

import com.galid.hemuser.domain.user.UserAccount
import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.domain.user.UserPrivateInformation
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.service.dto.UserDto
import com.galid.hemuser.service.dto.UserDto.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val loginService: LoginService,
    private val encoder: PasswordEncoder,
    private val tokenService: TokenService
) {
    @Transactional(readOnly = false)
    fun join(
        request: JoinRequest
    ): JoinResponse {
        val newUser = UserEntity(
            userAccount = UserAccount(
                accountId = request.accountId,
                password = encoder.encode(request.password),
            ),
            userInformation = UserPrivateInformation(
                profileImageUrl = request.profileImageUrl,
                nickName = request.nickName,
                email = request.email
            )
        )

        val savedUser = userRepository.save(newUser)

        return JoinResponse(
            id = savedUser.id!!,
        )
    }

    @Transactional(readOnly = false)
    fun login(
        request: LoginRequest
    ): LoginResponse {
        // user 찾기
        val foundUser = userRepository.findByUserAccount_AccountId(request.accountId)
            .orElseThrow { throw RuntimeException("존재하지 않는 ID입니다.") }

        // login 정보 검증
        loginService.validateLoginInfo(
            requestPassword = request.password,
            storedPassword = foundUser.userAccount.password
        )

        // token 발급
        val refreshToken = tokenService.createRefreshToken(foundUser.id!!)
        val authToken = tokenService.renewAuthToken(refreshToken)

        return LoginResponse(
            id = foundUser.id!!,
            refreshToken = refreshToken,
            authToken = authToken
        )
    }

    fun getUser(
        userId: Long
    ): UserResponse {
        val userEntity = userRepository.findById(userId)
            .orElseThrow { throw RuntimeException("존재하지 않는 유저입니다.") }

        return UserResponse(
            id = userEntity.id!!,
            userInformation = UserPrivateInformationDto(
                profileImageUrl = userEntity.userInformation.profileImageUrl,
                nickName = userEntity.userInformation.nickName,
                email = userEntity.userInformation.email
            )
        )
    }
}