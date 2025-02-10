package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable


@Serializable
data class ShoppingListItemModelFull(

   // @ColumnInfo(name = "list_id")
    var listId: Int,

   // @ColumnInfo(name = "user_id")
    var userId: String,


    /*
    @Relation(
        entity = Product::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "ean", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "ean", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )*/
    val product: Product,
)