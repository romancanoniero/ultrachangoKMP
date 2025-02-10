package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable
import org.koin.core.scope.ScopeID

@Serializable
data class ShoppingListMemberFull(

  //  @ColumnInfo(name = "list_id")
    var listId: Int,

 //   @ColumnInfo(name = "user_id")
    var userId: String,
/*
    @Relation(
        entity = User::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "user_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "user_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
    */
    val user: User,
)