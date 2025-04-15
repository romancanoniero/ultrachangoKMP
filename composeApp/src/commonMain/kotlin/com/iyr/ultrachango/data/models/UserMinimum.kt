package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
// @Entity("users")
data class UserMinimum(
 //   @PrimaryKey
 //   @ColumnInfo(name = "user_id")
    var userKey: String,
   // @ColumnInfo(name = "nick")

    @Transient  var displayName: String? = null,

    @Transient  var profilePicturePath: String? = null,

    ) {

    constructor() : this("", "", "")

    constructor(id : String) : this(userKey = id, displayName = "", profilePicturePath = "")

}