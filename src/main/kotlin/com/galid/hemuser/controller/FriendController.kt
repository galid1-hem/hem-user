package com.galid.hemuser.controller

import com.galid.hemuser.service.usecase.FriendUseCase
import com.galid.hemuser.service.dto.FriendDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
    private val friendUseCase: FriendUseCase
) {
    // TODO PAGINATION
    @GetMapping("/{userId}")
    fun getFriendList(
        @PathVariable("userId") userId: Long,
        @RequestParam(name = "size", required = false) size: Int?,
        @RequestParam(name = "lastFriendId", required = false) lastFriendId: Long?
    ): Response<FriendDto.FriendsResponse> {
        return Response(
            data = friendUseCase.getFriendList(userId)
        )
    }

    @PostMapping("/{userId}/{friendId}")
    fun makeFriend(
        @PathVariable("userId") userId: Long,
        @PathVariable("friendId") friendId: Long,
    ): Response<Void> {
        friendUseCase.makeFriend(
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
        friendUseCase.unFriend(
            userId = userId,
            friendId = friendId
        )
        return Response()
    }
}