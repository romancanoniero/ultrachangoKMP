package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable

data class ShoppingListComplete(

  //  @Ignore
  //  @Embedded
    val shoppingListModel: ShoppingList? = null,

    var listId: Int? = null,

    var userId: String? = null,

    var listName: String? = null,

    var imageUrl: String? = null,

    var creationTime: Long? = 0,

    var creationTimestamp: String? = null,

    var updateTime: Long? = 0,

    var updateTimestamp: String? = null,

    //@Ignore

    val id: Long? = null,
/*
    @Relation(
        entity = User::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "user_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "user_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
*/

    val user: User? = null,
/*
    @Relation(
        entity = ShoppingListProduct::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "list_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "list_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
*/
 //   @Ignore
    val items: List<ShoppingListProductComplete>? = null,


   /*
    @Relation(

        entity = ShoppingListMember::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "list_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "list_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
    @Ignore
    */

    val members: List<ShoppingListMemberComplete>? = null,


    ) {
    fun getId(): Int {
        return listId!!
    }

    fun toShoppingList(): ShoppingList {
        return ShoppingList(
            listId = listId,
            userId = userId,
            listName = listName,
            imageUrl = imageUrl,
            members = members,
 //           members = members?.map { memberComplete ->
 //               ShoppingListMember(listId = listId!!, userId = memberComplete.userId)
 //           } ?: emptyList(),
            items = items?.map { productComplete ->
                ShoppingListProduct(
                    listId = listId!!,
                    ean = productComplete.product?.ean!!,
                    quantities = productComplete.quantities
                )
            } ?: emptyList(),
            creationTimestamp = creationTimestamp,
            updateTime = updateTime!!,
            updateTimestamp = updateTimestamp
        )
    }
}

@Serializable
data class ShoppingListMemberComplete(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "user_id")
    var userId: String,
/*
    @Relation(
        entity = User::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "user_id", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "user_id", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
 */

    val user: User? = null,


)
{
    fun toShoppingListMember(): ShoppingListMember {
        return ShoppingListMember(
            listId = listId,
            userId = userId,
            user = user
        )
    }
}


@Serializable
/*
@Entity(
    "shopping_list_products",
    primaryKeys = ["list_id", "ean"],

    )

 */
data class ShoppingListProductComplete(

 //   @Embedded
    val shoppingListModel: ShoppingListProduct? = null,
 /*
   @Relation(
        entity = Product::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "ean", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "ean", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )
  */
    val product: Product? = null,


/*
    @Relation(
        entity = ShoppingListQuantities::class, /* The class of the related table(entity) (the children)*/
        parentColumn = "ean", /* The column in the @Embedded class (parent) that is referenced/mapped to */
        entityColumn = "ean", /* The column in the @Relation class (child) that is referenced (many-many) or references the parent (one(parent)-many(children)) */
        /* For the mapping table */
    )

 */
    var quantities: List<ShoppingListQuantities>? = null,

    )


//-------------
//@Entity("shopping_list")
@Serializable
data class ShoppingList(

   // @ColumnInfo(name = "list_id")
    var listId: Int? = null,

    //@ColumnInfo(name = "user_id")
    var userId: String? = null,

   // @ColumnInfo(name = "list_name")
    var listName: String? = null,

//    @ColumnInfo(name = "name") var name: String? = null,

    //@ColumnInfo(name = "image_url")
    var imageUrl: String? = null,

    //@Ignore
    val items: List<ShoppingListProduct>? = null,

   // @Ignore
    val members: List<ShoppingListMemberComplete>? = null,

    //@ColumnInfo(name = "creation_timestamp")
    var creationTimestamp: String? = null,

    //@ColumnInfo(name = "update_time")
    var updateTime: Long = 0,

    //@ColumnInfo(name = "update_timestamp")

    var updateTimestamp: String? = null,


    ) {
 //   @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


@Serializable
/*
@Entity(
    "shopping_list_products",
    primaryKeys = ["list_id", "ean"],

    )

 */
data class ShoppingListProduct(
    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "ean")
    var ean: String,

    //@Ignore
    val quantities: List<ShoppingListQuantities>? = null,
) {
    constructor() : this(0, "")

}

@Serializable
/*
@Entity(
    "shopping_list_quantities",
    primaryKeys = ["list_id", "ean", "user_id"],

    )*/
data class ShoppingListQuantities(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "ean")
    var ean: String,

    //@ColumnInfo(name = "user_id")
    var userId: String,

    //@ColumnInfo(name = "qty")
    var qty: Double,

    )


@Serializable
/*
@Entity(
    "shopping_list_members",
    primaryKeys = ["list_id", "user_id"],

    )

 */
data class ShoppingListMember(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "user_id")
    var userId: String,

    //@Ignore
    var user: User? = null,

    //@ColumnInfo(name = "is_admin")
    var isAdmin : Boolean = false,

    var connectionStatus: String? = null
) {
    fun toShoppingListMemberComplete() : ShoppingListMemberComplete {
        return ShoppingListMemberComplete(
            listId = listId,
            userId = userId,
            user = user
        )

    }

    constructor() : this(0, "")
}