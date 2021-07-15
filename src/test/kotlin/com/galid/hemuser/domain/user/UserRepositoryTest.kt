package com.galid.hemuser.domain.user

import com.galid.hemuser.TEST_ACCOUNT_ID
import com.galid.hemuser.getMockUserEntity
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test
import kotlin.test.assertEquals

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    lateinit var em: TestEntityManager
    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var mockUser: UserEntity

    @BeforeEach
    fun init() {
        em.clear()

        mockUser = getMockUserEntity()
        userRepository.save(mockUser)
    }

    @Test
    fun `find by UserAccount userId`() {
        val userEntity = userRepository.findByUserAccount_AccountId(TEST_ACCOUNT_ID).get()

        assertEquals(userEntity, mockUser)
    }

}