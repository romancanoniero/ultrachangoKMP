package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable


enum class RecordType {
    APP_USER,
    INVITE_SUGGESTION
}

@Serializable
//@Entity("users")
data class UserAsFamilyMember(
 //   @PrimaryKey
//    @ColumnInfo(name = "user_id")
    var id: String,
  //  @ColumnInfo(name = "nick")
    var nick: String,
    var name: String?  = null,
    var extras: String?  = null,
    var fileName: String?  = null,

    var recordType: RecordType = RecordType.APP_USER,

    )



