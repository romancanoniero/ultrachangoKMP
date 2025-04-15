package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable

@Serializable
// @Entity("users")
data class ShoppingCartUsers(

    var userKey: String,

    var displayName: String? = null,

    var profilePicturePath: String? = null,

    var isAdmin: Boolean? = false,

    var canModify: Boolean? = false,

    ) {
    fun toUserMinimum(): UserMinimum {
        return UserMinimum(
            userKey = this.userKey,
            displayName = this.displayName,
            profilePicturePath = this.profilePicturePath
        )
    }

    constructor() : this("", "", "", false, false)

    constructor(userKey: String) : this(
        userKey = userKey,
        displayName = "",
        profilePicturePath = "",
    )

}