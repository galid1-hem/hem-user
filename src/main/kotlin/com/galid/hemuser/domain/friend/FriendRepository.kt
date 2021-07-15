package com.galid.hemuser.domain.friend

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FriendRepository: JpaRepository<FriendEntity, Long> {
    fun findByUserId(userId: Long): List<FriendEntity>
    fun findByUserIdAndFriendId(userId: Long, friendId: Long): Optional<FriendEntity>
}