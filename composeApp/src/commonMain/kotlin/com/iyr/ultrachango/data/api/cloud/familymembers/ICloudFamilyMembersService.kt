package com.iyr.ultrachango.data.api.cloud.familymembers

import com.iyr.ultrachango.data.models.FamilyMember
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete

interface ICloudFamilyMembersService  {

 //   suspend fun save(shoppingList : ShoppingList): ShoppingList?

    suspend fun get(userKey : String): FamilyMember

    suspend fun getList(userKey : String): List<FamilyMember>



}