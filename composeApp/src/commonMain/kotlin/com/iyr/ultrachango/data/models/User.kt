package com.iyr.ultrachango.data.models


import com.iyr.ultrachango.data.models.enums.Genders
import kotlinx.serialization.Serializable

@Serializable
// @Entity("users")
data class User(
 //   @PrimaryKey
 //   @ColumnInfo(name = "user_id")
    var uid: String,
   // @ColumnInfo(name = "nick")


//    @ColumnInfo(name = "first_name")
    var firstName: String?  = null,
//    @ColumnInfo(name = "last_name")
    var lastName: String?  = null,

    var displayName: String? = firstName,

    var profilePicturePath: String? = null,

//    @ColumnInfo(name = "email_address")
    var email: String?  = null,
//    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null,

 //   @ColumnInfo(name = "is_anonymous")
    var isAnonymous: Boolean? = true,
 //   @ColumnInfo(name = "birth_date")
    var birthDate: String? = null,
 //   @ColumnInfo(name = "gender")
   // var gender: Int? = null,
    var gender: String = Genders.UNKNOWN.name



) {

    constructor() : this("", "", "", "", "", "", "", true, "", Genders.UNKNOWN.name)

    constructor(id : String) : this(uid = id, displayName = "", firstName = "", lastName = "", profilePicturePath = "",  email = "", phoneNumber = "", isAnonymous = true, birthDate = "",)

}