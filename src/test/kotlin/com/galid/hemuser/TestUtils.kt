package com.galid.hemuser

import com.galid.hemuser.domain.friend.FriendEntity
import com.galid.hemuser.domain.user.UserAccount
import com.galid.hemuser.domain.user.UserEntity
import com.galid.hemuser.domain.user.UserPrivateInformation
import com.galid.hemuser.service.dto.UserDto
import com.galid.hemuser.service.dto.UserDto.LoginRequest

val TEST_ACCOUNT_ID = "TEST"
val TEST_PASSWORD = "TEST"
val TEST_NICK_NAME = "TEST"
val TEST_PROFILE_IMAGE_URL = "TEST"
val TEST_EMAIL = "TEST"


// ######## USER ########
fun getMockUserCreateRequest(
    accountId: String = TEST_ACCOUNT_ID,
    password: String = TEST_PASSWORD,
    email: String? = TEST_EMAIL,
    profileImageUrl: String? = TEST_PROFILE_IMAGE_URL,
    nickName: String? = TEST_NICK_NAME
): UserDto.JoinRequest {
    return UserDto.JoinRequest(
        accountId = accountId,
        password = password,
        email = email,
        profileImageUrl = profileImageUrl,
        nickName = nickName
    )
}

fun getMockUserEntity(
    id: Long ?= 1L,
    accountId: String= TEST_ACCOUNT_ID,
    password: String= TEST_PASSWORD,
    email: String= TEST_EMAIL,
    profileImageUrl: String= TEST_PROFILE_IMAGE_URL,
    nickName: String= TEST_NICK_NAME,
    userStatus: UserEntity.UserStatus= UserEntity.UserStatus.ACTIVE
): UserEntity {
    return UserEntity(
        id = id,
        userAccount = UserAccount(
            accountId = accountId,
            password = password,
        ),
        userInformation = UserPrivateInformation(
            profileImageUrl = profileImageUrl,
            nickName = nickName,
            email = email
        ),
        userStatus = userStatus
    )
}

fun getMockLoginRequest(
    accountId: String = TEST_ACCOUNT_ID,
    password: String = TEST_PASSWORD
): LoginRequest {
   return LoginRequest(
       accountId = accountId,
       password = password
   )
}

// ######## FRIEND ###########
fun getMockFriendEntity(
    userId: Long,
    friendId: Long
): FriendEntity {
    return FriendEntity(
        userId = userId,
        friendId = friendId
    )
}