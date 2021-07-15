package com.galid.hemuser.domain.friend

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(
            name = "user_id_idx",
            columnList = "userId"
        ),
        Index(
            name = "user_friend_unfriend_idx",
            columnList = "userId, friendId, unFriendAt"
        )
    ],
    uniqueConstraints = [
        UniqueConstraint(
            name = "user_id_friend_id",
            columnNames = ["userId", "friendId"]
        )
    ]
)
class FriendEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    val userId: Long,
    val friendId: Long,
    private var unFriendAt: LocalDateTime? = null
) {
    val unFriend: Boolean
        get() = unFriendAt != null

    fun unFriend() {
        this.unFriendAt = LocalDateTime.now()
    }

    fun reFriend() {
        this.unFriendAt = null
    }
}