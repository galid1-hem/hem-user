package com.galid.hemuser.domain.user

import com.galid.hemuser.domain.BaseAuditEntity
import com.galid.hemuser.domain.user.UserEntity.UserStatus.ACTIVE
import javax.persistence.*

@Entity
@Table(indexes = [
    Index(name = "id_account_id_idx", columnList = "id, accountId")
])
class UserEntity(
    @Id
    @GeneratedValue
    var id: Long ?= null,
    @Embedded
    val userAccount: UserAccount,
    @Embedded
    val userInformation: UserPrivateInformation,
    @Enumerated(EnumType.ORDINAL)
    val userStatus: UserStatus = ACTIVE
): BaseAuditEntity() {
    enum class UserStatus {
        BLOCK, ACTIVE
    }
}
