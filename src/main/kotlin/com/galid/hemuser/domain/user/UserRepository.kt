package com.galid.hemuser.domain.user

import com.galid.hemuser.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByUserAccount_AccountId(accountId: String): Optional<UserEntity>
}
