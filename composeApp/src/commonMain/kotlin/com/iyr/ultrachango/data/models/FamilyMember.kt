package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable
import kotlin.jvm.Transient

@Serializable
//@Entity("family_members")
data class FamilyMember(
  //  @PrimaryKey
  //  @ColumnInfo(name = "user_id")
    @Transient var userId: String,

  //  @PrimaryKey
  //  @ColumnInfo(name = "member_id")
    @Transient var memberId: String?,
/*
    @Relation(
        entity = User::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "member_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "user_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )

 */
    var user: UserAsFamilyMember,



   // @ColumnInfo(name = "connection_status")
    var connectionStatus: String,

//    @ColumnInfo(name = "creation_time")
    var creationTime: Long?  = null,

  //  @ColumnInfo(name = "creation_timestamp")
    var creationTimeStamp: String?  = null,


//    @ColumnInfo(name = "update_time")
    var updateTime: Long?  = null,

  //  @ColumnInfo(name = "update_timestamp")
    var updateTimeStamp: String?  = null,


    )