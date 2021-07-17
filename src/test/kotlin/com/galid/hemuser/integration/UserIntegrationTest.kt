package com.galid.hemuser.integration

import com.galid.hemuser.*
import com.galid.hemuser.domain.user.UserAccount
import com.galid.hemuser.domain.user.UserPrivateInformation
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.service.usecase.UserUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
    @Autowired
    lateinit var userUseCase: UserUseCase
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `audit info not null`() {
        val savedUser = userRepository.save(getMockUserEntity())

        assertNotNull(savedUser.createdAt)
        assertNotNull(savedUser.updatedAt)
    }

    @Test
    fun `join`() {
        // given
        val request = getMockUserCreateRequest(
            accountId = TEST_ACCOUNT_ID,
            password = TEST_PASSWORD)

        // when
        val createdUserId = userUseCase.join(request).id
        val foundUser = userRepository.findById(createdUserId)
            .get()

        // then
        assertEqualUserAccount(
            userId = request.accountId,
            password = request.password,
            foundUserAccount = foundUser.userAccount
        )
        assertEqualUserPrivateInfo(
            nickName = request.nickName?:TEST_NICK_NAME,
            profileImageUrl = request.profileImageUrl?:TEST_PROFILE_IMAGE_URL,
            email = request.email?:TEST_EMAIL,
            foundUserPrivateInfo = foundUser.userInformation
        )
    }

    internal fun assertEqualUserAccount(
        userId: String,
        password: String,
        foundUserAccount: UserAccount
    ) {
        assertEquals(userId, foundUserAccount.accountId)
        // password encder match
        println("isMatch? : " + passwordEncoder.matches(password, foundUserAccount.password))
        assertTrue(passwordEncoder.matches(password, foundUserAccount.password))
    }

    internal fun assertEqualUserPrivateInfo(
        nickName: String,
        profileImageUrl: String,
        email: String,
        foundUserPrivateInfo: UserPrivateInformation
    ) {
        assertEquals(nickName, foundUserPrivateInfo.nickName)
        assertEquals(profileImageUrl, foundUserPrivateInfo.profileImageUrl)
        assertEquals(email, foundUserPrivateInfo.email)
    }
}
