package com.iyr.ultrachango.data.api.cloud.shoppinglist

import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMember

interface ICloudShoppingListService  {

    suspend fun save(shoppingList : ShoppingList): ShoppingList?

    suspend fun get(userKey : String, id: Int): ShoppingListComplete

    suspend fun getAll(userKey : String): List<ShoppingListComplete>

    suspend fun delete(userKey : String, id: Int): Boolean?



    suspend fun addProductToList(listId: Int, ean: String, userId: String, quantity: Int)

    suspend fun removeProductFromShoppingList(id: Int, ean: String)

    fun getListProducts(listId: Int): List<ShoppingListProduct>

    suspend fun updateProductQuantityOnList(listId: Int, ean: String, userId: String, value: Double)


    suspend  fun getMembers(listId: Long): List<ShoppingListMember>

    suspend fun getMembers(listId: Long, userId: String): List<ShoppingListMember>

    suspend fun addMember(listId: Long, userId: String)
    suspend fun removeMember(listId: Long, userId: String)
    fun count(): Int
}