package com.iyr.ultrachango.data.models


import androidx.compose.ui.graphics.Outline
import com.iyr.ultrachango.utils.extensions.isDigitsOnly
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class ShoppingCart(

    /*
    //  @Ignore
    //  @Embedded
    val shoppingListModel: ShoppingList? = null,
*/
    val shoppingCartId: Int? = null,

    var userKey: String? = null,

    var listsIncluded: ArrayList<ShoppingCartShoppingList>? = ArrayList<ShoppingCartShoppingList>(),

    var creationTime: Long? = 0,

    var creationTimestamp: String? = null,

    var updateTime: Long? = 0,

    var updateTimestamp: String? = null,

    val user: UserMinimum? = null,

    val items: ArrayList<ShoppingCartProduct>? = ArrayList(),

    val generics: ArrayList<ShoppingCartGeneric>? = ArrayList(),


    val members: ArrayList<ShoppingCartUsers>? = ArrayList(),


    ) {


    fun includeFromList(list: ShoppingListComplete): ShoppingCart {
        if (list.listId?.toInt() !in this.listsIncluded?.map { it.shoppingListId.toInt() }
                .orEmpty()) {
            list.items?.forEach { listItem ->
                val item = items?.find { it?.ean == listItem.product?.ean }
                if (item == null) {

  //                  if (listItem.product?.ean.toString().isDigitsOnly()) {
                        items?.add(listItem.toShoppingCartProduct())

                   /*
                    } else {
                        /*
                        val generic = ShoppingCartGeneric(
                            ean = listItem.product?.ean,
                            name = listItem.product?.name,
                            qty = 1.0
                        )

                         */
                        generics?.add(listItem.toShoppingCartProduct())
                    }
*/

                }
            }
            list.listId?.toInt()?.let {
                this.listsIncluded?.add(
                    ShoppingCartShoppingList(
                        //    shoppingCartId = list.userId.toString(),
                        shoppingListId = it
                    )
                )
            }
        }
        return this
    }

    fun removeFromList(
        listToRemove: ShoppingListComplete,
        availableShoppingLists: List<ShoppingListComplete>
    ): ShoppingCart? {

        var otherLists = availableShoppingLists.filter { it.listId != listToRemove.listId }
//        otherLists = otherLists.filter { it.listId?.toInt() in listsIncluded.orEmpty() }
        otherLists = otherLists.filter {
            it.listId?.toInt() in listsIncluded?.map { it.shoppingListId.toInt() }.orEmpty()
        }

        val itemsToRemove = ArrayList<ShoppingListProductComplete>()

        listToRemove.items?.forEach { item ->
            var existsInOtherList = false

            otherLists.forEach {
                if (it.items?.find { it.ean == item.ean } != null) {
                    existsInOtherList = true
                }
            }
            if (!existsInOtherList) {
                itemsToRemove.add(item)
            }
        }
        itemsToRemove.forEach {
            items?.remove(it.toShoppingCartProduct())
        }

        val itemToRemove =
            listsIncluded?.find { it.shoppingListId.toInt() == listToRemove.listId?.toInt() }
        listsIncluded?.remove(itemToRemove)

        return this
    }

    fun prepateToSave(): ShoppingCart {
        val newShoppingCart = this.copy()

        newShoppingCart.items?.forEach { listItem ->
            if (listItem.ean.toString().isDigitsOnly() == false)
            {
                newShoppingCart.generics?.add(listItem.toProductGeneric())
            }
        }

        newShoppingCart.items?.removeAll { listItem ->
            listItem.ean.toString().isDigitsOnly() == false
        }

        return newShoppingCart
    }
}


/*
@Serializable
data class ShoppingListMemberComplete(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "user_id")
    var userId: String,

    val user: User? = null,


    ) {
    fun toShoppingListMember(): ShoppingListMember {
        return ShoppingListMember(
            listId = listId,
            userId = userId,
            user = user
        )
    }
}


@Serializable
data class ShoppingListProductComplete(

    //   @Embedded
    val shoppingListModel: ShoppingListProduct? = null,

    val ean: String ,
    var product: Product? = null,
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

data class ShoppingListQuantities(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "ean")
    var ean: String,

    //@ColumnInfo(name = "user_id")
    var userId: String,

    //@ColumnInfo(name = "qty")
    var qty: Double,

    var user: User? = null,
)


@Serializable

data class ShoppingListMember(

    //@ColumnInfo(name = "list_id")
    var listId: Int,

    //@ColumnInfo(name = "user_id")
    var userId: String,

    //@Ignore
    var user: User? = null,

    //@ColumnInfo(name = "is_admin")
    var isAdmin: Boolean = false,

    var connectionStatus: String? = null
) {
    fun toShoppingListMemberComplete(): ShoppingListMemberComplete {
        return ShoppingListMemberComplete(
            listId = listId,
            userId = userId,
            user = user
        )

    }

    constructor() : this(0, "")
}
*/
fun ShoppingCart.toJson(): String {
    return Json.encodeToString(this)
}
