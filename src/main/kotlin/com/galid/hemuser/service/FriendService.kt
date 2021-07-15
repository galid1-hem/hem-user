package com.galid.hemuser.service

import com.galid.hemuser.domain.friend.FriendEntity
import com.galid.hemuser.domain.friend.FriendRepository
import com.galid.hemuser.service.dto.FriendDto.FriendsResponse
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class FriendService(
    private val userService: UserService,
    private val friendRepository: FriendRepository
) {
    fun getFriendList(
        userId: Long
    ): FriendsResponse {
        validateExistUser(userId)

        val friendIdList = friendRepository.findByUserId(userId)
            .map { it.friendId }

        return FriendsResponse(
            friendIdList = friendIdList
        )
    }

    fun makeFriend(
        userId: Long,
        friendId: Long
    ) {
        val foundFriendEntity = friendRepository.findByUserIdAndFriendId(
            userId = userId,
            friendId = friendId
        )

        if (foundFriendEntity.isPresent) {
            foundFriendEntity.get().reFriend()
        }
        else {
            friendRepository.save(FriendEntity(userId = userId, friendId = friendId))
        }
    }

    fun unFriend(
        userId: Long,
        friendId: Long
    ) {
        validateExistUser(userId)

        val foundFriendEntity = friendRepository.findByUserIdAndFriendId(userId, friendId)
            .orElseThrow { throw RuntimeException("이미 친구가 아닙니다.") }

        foundFriendEntity.unFriend()
    }

    private fun validateExistUser(userId: Long) {
        userService.getUser(userId)
    }
}