package com.galid.hemuser.service

import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.getMockLoginRequest
import com.galid.hemuser.getMockUserCreateRequest
import com.galid.hemuser.getMockUserEntity
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeast
import org.mockito.kotlin.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserServiceTest {
    lateinit var userService: UserService

    lateinit var userRepository: UserRepository
    lateinit var loginService: LoginService
    lateinit var encoder: PasswordEncoder
    lateinit var tokenService: TokenService

    val TEST_ENCODED_PASSWORD = "TEST_ENCODED_PASSWORD"
    val TEST_REFRESH_TOKEN = "TEST_REFRESH_TOKEN"
    val TEST_AUTH_TOKEN = "TEST_AUTH_TOKEN"

    @BeforeAll
    fun init() {
        userRepository = mock()
        loginService = mock()
        encoder = mock()
        tokenService = mock()

        userService = UserService(
            userRepository = userRepository,
            loginService = loginService,
            encoder = encoder,
            tokenService = tokenService
        )
    }

    @Test
    fun `회원가입 시 save 와 encode가 호출되어야 한다`() {
        // given
        val captor = ArgumentCaptor.forClass(UserEntity::class.java)
        `when`(userRepository.save(any()))
            .thenReturn(getMockUserEntity())
        `when`(encoder.encode(any()))
            .thenReturn(TEST_ENCODED_PASSWORD)

        // when
        val joinRequest = getMockUserCreateRequest()
        userService.join(joinRequest)

        // then
        verify(userRepository, atLeastOnce())
            .save(captor.capture())
        verify(encoder, atLeastOnce())
            .encode(any())
    }

    @Test
    fun `로그인 시 login 정보 검증과 token 발급이 되어야 한다`() {
        // given
        `when`(userRepository.findByUserAccount_AccountId(any()))
            .thenReturn(Optional.of(getMockUserEntity()))
        doNothing()
            .`when`(loginService)
            .validateLoginInfo(any(), any())
        `when`(tokenService.createRefreshToken(any()))
            .thenReturn(TEST_REFRESH_TOKEN)
        `when`(tokenService.renewAuthToken(TEST_REFRESH_TOKEN))
            .thenReturn(TEST_AUTH_TOKEN)

        // when
        userService.login(getMockLoginRequest())

        // then
        verify(userRepository, atLeastOnce())
            .findByUserAccount_AccountId(any())
        verify(loginService, atLeastOnce())
            .validateLoginInfo(any(), any())
        verify(tokenService, atLeastOnce())
            .createRefreshToken(any())
        verify(tokenService, atLeastOnce())
            .renewAuthToken(any())
    }
    
    @Test
    fun `유저 정보 요청 시 findById가 호출되어야 한다`() {
        // given
        `when`(userRepository.findById(any()))
            .thenReturn(Optional.of(getMockUserEntity()))

        // when
        userService.getUser(any())
        
        // then
        verify(userRepository, atLeastOnce())
            .findById(any())
    }
}