package com.galid.hemuser.infra

import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.getMockUserEntity
import com.galid.hemuser.infra.TokenServiceImpl
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import java.util.*
import kotlin.test.*

@SpringBootTest
internal class TokenServiceImplTest {
    @SpyBean
    lateinit var tokenServiceImpl: TokenServiceImpl

    @MockBean
    lateinit var userRepository: UserRepository

    val TEST_USER_ID = 1L
    val TEST_USER_ENTITY = getMockUserEntity(id = TEST_USER_ID)

    @BeforeTest
    fun init() {
        // mocking userRepository
        `when`(userRepository.findById(TEST_USER_ID))
            .thenReturn(Optional.of(TEST_USER_ENTITY))
    }

    @Test
    fun `create RefreshToken and AuthToken`() {
        val refreshToken = tokenServiceImpl.createRefreshToken(TEST_USER_ID)
        val authToken = tokenServiceImpl.renewAuthToken(refreshToken)

        assertNotNull(refreshToken)
        assertNotNull(authToken)
    }

    @Test
    fun `fail verify token when receiving expired token`() {
        val pastDate = Date(Date().time - 1000)

        // mocking getExpiration
        given(tokenServiceImpl.getExpiration(TokenServiceImpl.AuthTokenType.REFRESH))
            .willReturn(pastDate)

        val expiredToken = tokenServiceImpl.createRefreshToken(TEST_USER_ID)

        assertFails { tokenServiceImpl.verifyToken(expiredToken) }
    }

    @Test
    fun `auth token contains userinformation`() {
        // when
        val refreshToken = tokenServiceImpl.createRefreshToken(TEST_USER_ID)
        val authToken = tokenServiceImpl.renewAuthToken(refreshToken)

        val decodedAuthToken = tokenServiceImpl.verifyToken(authToken)
            .body
        val userInformation = TEST_USER_ENTITY.userInformation

        // then
        assertEquals(TEST_USER_ID.toInt(), decodedAuthToken.get(TokenServiceImpl.USER_ID_KEY))
        assertEquals(userInformation.email, decodedAuthToken.get(TokenServiceImpl.USER_EMAIL_KEY))
        assertEquals(userInformation.nickName, decodedAuthToken.get(TokenServiceImpl.USER_NICKNAME_KEY))
        assertEquals(userInformation.profileImageUrl, decodedAuthToken.get(TokenServiceImpl.USER_PROFILE_IMAEGE_KEY))
    }

    // 토큰을 찾는것이 더 빠르지만 차이는 미미하며, 따라서 데이터베이스에 굳이 저장하지 않고 반환해도 될 것 같다
//    @Test
//    fun `토큰을 새로 생성하는 것과 db를 조회하는것중 뭐가 더 빠를까`() {
//        val startTime = System.currentTimeMillis()
//        for (i in 0 .. 100) {
//            tokenServiceImpl.createRefreshToken(TEST_USER_ID)
//        }
//        println("create time : " + (System.currentTimeMillis() - startTime))
//
//        val start2Time = System.currentTimeMillis()
//        for (i in 0 .. 100) {
//            userRepository.findById(TEST_USER_ID)
//        }
//        println("find time : " + (System.currentTimeMillis() - start2Time))
//    }
}