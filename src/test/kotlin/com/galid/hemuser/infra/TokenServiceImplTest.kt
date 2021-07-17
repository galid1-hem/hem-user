package com.galid.hemuser.infra

import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.infra.TokenServiceImpl
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.core.io.ClassPathResource
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull

@SpringBootTest
internal class TokenServiceImplTest {
    @SpyBean
    lateinit var tokenServiceImpl: TokenServiceImpl
    @Autowired
    lateinit var userRepository: UserRepository

    val TEST_USER_ID = 1L

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
    fun `auth token contains userId`() {
        val refreshToken = tokenServiceImpl.createRefreshToken(TEST_USER_ID)
        val authToken = tokenServiceImpl.renewAuthToken(refreshToken)

        val decodedAuthToken = tokenServiceImpl.verifyToken(authToken)

        assertEquals(TEST_USER_ID.toInt(), decodedAuthToken.body.get("userId"))
    }

    // 토큰을 찾는것이 더 빠르지만 차이는 미미하며, 따라서 데이터베이스에 굳이 저장하지 않고 반환해도 될 것 같다
    @Test
    fun `토큰을 새로 생성하는 것과 db를 조회하는것중 뭐가 더 빠를까`() {
        val startTime = System.currentTimeMillis()
        for (i in 0 .. 100) {
            tokenServiceImpl.createRefreshToken(TEST_USER_ID)
        }
        println("create time : " + (System.currentTimeMillis() - startTime))

        val start2Time = System.currentTimeMillis()
        for (i in 0 .. 100) {
            userRepository.findById(TEST_USER_ID)
        }
        println("find time : " + (System.currentTimeMillis() - start2Time))
    }
}