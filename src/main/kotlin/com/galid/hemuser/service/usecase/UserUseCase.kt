package com.galid.hemuser.service.usecase

import com.galid.hemuser.domain.user.UserAccount
import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.domain.user.UserPrivateInformation
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.service.LoginService
import com.galid.hemuser.service.TokenService
import com.galid.hemuser.service.dto.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserUseCase(
    private val userRepository: UserRepository,
    private val loginService: LoginService,
    private val encoder: PasswordEncoder,
    private val tokenService: TokenService
) {
    @Transactional(readOnly = false)
    fun join(
        request: UserDto.JoinRequest
    ): UserDto.JoinResponse {
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

        return UserDto.JoinResponse(
            id = savedUser.id!!,
        )
    }

    @Transactional(readOnly = false)
    fun login(
        request: UserDto.LoginRequest
    ): UserDto.LoginResponse {
        // login 정보 검증
        val foundUser = loginService.validateLoginInfo(request)

        // token 발급
        val refreshToken = tokenService.createRefreshToken(foundUser.id!!)
        val authToken = tokenService.renewAuthToken(refreshToken)

        return UserDto.LoginResponse(
            id = foundUser.id!!,
            refreshToken = refreshToken,
            authToken = authToken
        )
    }

    fun getUser(
        userId: Long
    ): UserDto.UserResponse {
        val userEntity = userRepository.findById(userId)
            .orElseThrow { throw RuntimeException("존재하지 않는 유저입니다.") }

        return fromUserEntity(userEntity)
    }

    internal fun fromUserEntity(
        userEntity: UserEntity
    ): UserDto.UserResponse {
        return UserDto.UserResponse(
            id = userEntity.id!!,
            userInformation = UserDto.UserPrivateInformationDto(
                profileImageUrl = userEntity.userInformation.profileImageUrl,
                nickName = userEntity.userInformation.nickName,
                email = userEntity.userInformation.email
            )
        )
    }
}