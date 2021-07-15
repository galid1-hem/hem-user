package com.galid.hemuser.domain.user

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class UserPrivateInformation(
    @Column
    var profileImageUrl: String? = null,
    var nickName: String? = null,
    var email: String? = null
)