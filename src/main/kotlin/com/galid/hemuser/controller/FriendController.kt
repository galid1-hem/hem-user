package com.galid.hemuser.controller

import com.galid.hemuser.service.FriendService
import com.galid.hemuser.service.dto.FriendDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
    private val friendService: FriendService
) {
    // TODO PAGINATION
    @GetMapping("/{userId}")
    fun getFriendList(
        @PathVariable("userId") userId: Long,
        @RequestParam(name = "size", required = false) size: Int?,
        @RequestParam(name = "lastFriendId", required = false) lastFriendId: Long?
    ): Response<FriendDto.FriendsResponse> {
        return Response(
            data = friendService.getFriendList(userId)
        )
    }

    @PostMapping("/{userId}/{friendId}")
    fun makeFriend(
        @PathVariable("userId") userId: Long,
        @PathVariable("friendId") friendId: Long,
    ): Response<Void> {
        friendService.makeFriend(
            userId = userId,
            friendId = friendId
        )
        return Response()
    }

    @DeleteMapping("/{userId}/{friendId}")
    fun unFriend(
        @PathVariable("userId") userId: Long,
        @PathVariable("friendId") friendId: Long,
    ): Response<Void> {
        friendService.unFriend(
            userId = userId,
            friendId = friendId
        )
        return Response()
    }
}