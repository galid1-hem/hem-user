package com.galid.hemuser.infra

import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.getMockLoginRequest
import com.galid.hemuser.getMockUserEntity
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.AdditionalMatchers.not
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFails

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LoginServiceImplTest {
    lateinit var loginServiceImpl: LoginServiceImpl

    lateinit var encoder: PasswordEncoder
    lateinit var userRepository: UserRepository

    val foundUser = getMockUserEntity()

    val VALID_PASSWORD = "PASSWORD"
    val NOT_VALID_PASSWORD = "NOT_VALID_PASSWORD"
    val ENCODED_PASSWORD = foundUser.userAccount.password

    val NOT_EXIST_USER_ACCOUNT_ID = "NOT_EXIST_USER_ID"
    val EXIST_USER_ACCOUNT_ID = "EXIST_USER_ID"


    @BeforeAll
    fun init() {
        // encoder mockgin
        encoder = mock()
        `when`(encoder.encode(VALID_PASSWORD))
            .thenReturn(ENCODED_PASSWORD)
        `when`(encoder.matches(ENCODED_PASSWORD, ENCODED_PASSWORD))
            .thenReturn(true)
        `when`(encoder.matches(eq(ENCODED_PASSWORD), not(eq(ENCODED_PASSWORD))))
            .thenThrow(RuntimeException("비밀번호가 일치하지 않습니다."))

        // repository mocking
        userRepository = mock()
        `when`(userRepository.findByUserAccount_AccountId(NOT_EXIST_USER_ACCOUNT_ID))
            .thenThrow(RuntimeException("존재하지 않는 ID입니다."))
        `when`(userRepository.findByUserAccount_AccountId(EXIST_USER_ACCOUNT_ID))
            .thenReturn(Optional.of(foundUser))

        //
        loginServiceImpl = LoginServiceImpl(encoder = encoder, userRepository = userRepository)
    }

    @Test
    fun `존재하지 않는 유저에 대한 요청시 exception`() {
        // when then
        assertFails {
            loginServiceImpl.validateLoginInfo(getMockLoginRequest(accountId = NOT_EXIST_USER_ACCOUNT_ID))
        }
    }

    @Test
    fun `일치하지 않는 비밀번호로 login시 exception`() {
        // when then
        assertFails {
            loginServiceImpl.validateLoginInfo(getMockLoginRequest(password = NOT_VALID_PASSWORD))
        }
    }

    @Test
    fun `올바른 정보 입력시 userEntity 반환`() {
        // given
        val validLoginRequest = getMockLoginRequest(
            accountId = EXIST_USER_ACCOUNT_ID,
            password = VALID_PASSWORD
        )

        // when
        val user = loginServiceImpl.validateLoginInfo(validLoginRequest)
        
        // then
        assertEquals(user, foundUser)
    }
}