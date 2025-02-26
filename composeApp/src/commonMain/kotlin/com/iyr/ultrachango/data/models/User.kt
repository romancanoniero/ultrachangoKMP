package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable

@Serializable
// @Entity("users")
data class User(
 //   @PrimaryKey
 //   @ColumnInfo(name = "user_id")
    var uid: String,
//    @ColumnInfo(name = "nick")
    var nick: String,

//    @ColumnInfo(name = "first_name")
    var firstName: String?  = null,
//    @ColumnInfo(name = "last_name")
    var lastName: String?  = null,

    var fileName: String? = null,

//    @ColumnInfo(name = "email_address")
    var email: String?  = null,
//    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null,

 //   @ColumnInfo(name = "is_anonymous")
    var isAnonymous: Boolean? = true,
 //   @ColumnInfo(name = "birth_date")
    var birthDate: String? = null,
 //   @ColumnInfo(name = "gender")
    var gender: Int? = null,




) {

    constructor() : this("", "", "", "", "", "", "", true, "", 0)

    constructor(id : String) : this(uid = id, nick = "", firstName = "", lastName = "", fileName = "",  email = "", phoneNumber = "", isAnonymous = true, birthDate = "",)

}