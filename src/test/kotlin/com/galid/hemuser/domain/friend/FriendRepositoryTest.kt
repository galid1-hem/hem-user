package com.galid.hemuser.domain.friend

import com.galid.hemuser.getMockFriendEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test
import kotlin.test.assertTrue

@DataJpaTest
class FriendRepositoryTest {
    @Autowired
    lateinit var friendRepository: FriendRepository

    @Test
    fun `get friend list`() {
        // given
        val userId = 1L
        val friendIdList = listOf(2L, 3L)
        val friendEntities = mutableListOf<FriendEntity>()
        for (friendId in friendIdList) {
            friendEntities.add(getMockFriendEntity(userId, friendId))
        }
        friendRepository.saveAll(friendEntities)

        // when
        val foundFriendIdList = friendRepository.findByUserId(userId)
            .map { it.friendId }

        // then
        assertTrue(foundFriendIdList.containsAll(friendIdList))
    }

}