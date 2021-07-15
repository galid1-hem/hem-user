package com.galid.hemuser.domain.user

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class UserAccount(
    @Column
    val accountId: String,
    val password: String
)