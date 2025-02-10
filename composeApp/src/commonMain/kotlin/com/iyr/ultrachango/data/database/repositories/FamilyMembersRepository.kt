package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.data.api.cloud.familymembers.CloudFamilyMembersService


import com.iyr.ultrachango.data.models.FamilyMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FamilyMembersRepository(
  //  private val shoppingListDao: ProductsDao,
    private val familyMembersCloudService: CloudFamilyMembersService,
) {

    fun getList(userKey: String): Flow<List<FamilyMember>> = flow {
  /*
        var localCall = shoppingListDao.getShoppingLists(userKey)
        emit(localCall)
*/
        val cloudCall = familyMembersCloudService.getList(userKey)
        emit(cloudCall)
        /*
        shoppingListDao.deleteAll()

        mapToShoppingList(cloudCall).forEach { shoppingList ->
            shoppingListDao.insertShoppingList(
                shoppingList,
                shoppingList.items,
                shoppingList.members
            )
        }

        var localCall = shoppingListDao.getShoppingLists(userKey)
        emit(localCall)

         */
    }

/*
    fun mapToShoppingList(shoppingListComplete: List<ShoppingListComplete>): List<ShoppingList> {

        val newList: ArrayList<ShoppingList> = ArrayList<ShoppingList>();
        shoppingListComplete.forEach {
            val shoppingList = ShoppingList(listId = it.listId,
                userId = it.userId,
                listName = it.listName,
                imageUrl = it.imageUrl,
                items = it.items?.map { productComplete ->
                    ShoppingListProduct(
                        it.listId?.toInt()!!,
                        productComplete.product?.ean!!,
                        productComplete.quantities
                    )
                } ?: emptyList(),

                members = it.members?.map { memberComplete ->
                    ShoppingListMember(it.listId?.toInt()!!, memberComplete.userId)
                } ?: emptyList(),

                creationTimestamp = it.shoppingListModel?.creationTimestamp,
                updateTime = it.shoppingListModel?.updateTime ?: 0,
                updateTimestamp = it.shoppingListModel?.updateTimestamp).apply {
                id = it.shoppingListModel?.id ?: 0
            }
            newList.add(shoppingList)
        }

        return newList
        /*
                return shoppingListComplete.map { complete ->
                    ShoppingList(listId = complete.listId,
                        userId = complete.userId,
                        //   name = complete.name,
                        imageUrl = complete.imageUrl,

                        items = complete.items?.map { productComplete ->
                            ShoppingListProduct(complete.listId?.toInt()!!, productComplete.product?.ean!!)
                        } ?: emptyList(),
                        creationTimestamp = complete.shoppingListModel?.creationTimestamp,
                        updateTime = complete.shoppingListModel?.updateTime ?: 0,
                        updateTimestamp = complete.shoppingListModel?.updateTimestamp).apply {
                        id = complete.shoppingListModel?.id ?: 0
                    }
                }

         */
    }
*/
    /*
        fun fetchItems(listId: Long): Flow<List<ShoppingListItemModel>> = flow {
            var localCall = shoppingListDao.fetchListProducts(listId).collect() {
                emit(it)
                    val cloudCall = shoppingListCloudService.getListProducts(listId)
                    val callRecords =
                        cloudCall.map { it } ?: emptyList()
                    // Filtrar productos nuevos que no están en la base de datos local
                //    shoppingListDao.insertHewItems(callRecords.toList())
            }
        }

    */

    /*
    suspend fun saveShoppingList(shoppingList: ShoppingList) {
        val userKey = shoppingList.userId.toString()
        try {
            shoppingListCloudService.save(shoppingList)?.let { newEntity ->

                shoppingListDao.insertShoppingList(newEntity,
                    newEntity.items?.map { it } ?: emptyList(),
                    newEntity.members?.map { it } ?: emptyList())

                shoppingListDao.saveShoppingList(newEntity)/*
                                //  val roomShoppingLists  = shoppingListDao.fetchShoppingList(userKey)
                                val cloudShoppingLists = shoppingListCloudService.getAll(userKey)
                                val newShoppingLists = cloudShoppingLists.filter { cloudShoppingList ->
                                    shoppingListDao.getShoppingLists(userKey)
                                        .none { localShoppingList ->
                                            localShoppingList.name == cloudShoppingList.name
                                                    && localShoppingList.creationTimestamp == cloudShoppingList.creationTimestamp
                                                    && localShoppingList.updateTimestamp == cloudShoppingList.updateTimestamp
                                        }
                                }
                                // Agregar los productos nuevos a la base de datos de Room
                                if (newShoppingLists.isNotEmpty()) {
                                    shoppingListDao.updateShoppingLists(newShoppingLists.toList())
                                }
                */
            }
        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }

    }

    suspend fun renameList(shoppingListId: Int, newName: String) {
        shoppingListDao.renameList(shoppingListId, newName)

        try {
            val entity = shoppingListDao.getShoppingList(shoppingListId)
            entity.listName = newName

            shoppingListCloudService.save(entity)?.let {
                shoppingListDao.saveShoppingList(it)
            }

        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }


    }


    suspend fun removeList(shoppingList: ShoppingList) {
        try {
            shoppingListCloudService.delete(shoppingList.userId.toString(), shoppingList.id)?.let {
                shoppingListDao.deleteShoppingList(shoppingList.id)
            }
        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }

    }

    suspend fun getShoppingList(userKey : String, shoppingListId: Int): ShoppingListComplete {
//        return shoppingListDao.getShoppingList(shoppingListId)

        return shoppingListCloudService.get(userKey, shoppingListId)
    }

    suspend fun countShoppingList(): Int = shoppingListDao.countShoppingList()


    suspend fun removeProductFromList(id: Int, ean: String) {
        shoppingListDao.removeProductFromList(id, ean)
    }

    suspend fun addProductToList(id: Int, ean: String, userKey: String, quantity: Int) {
        try {
            shoppingListCloudService.addProductToList(id, ean, userKey, quantity)
            shoppingListDao.addProductToList(id, ean)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateProductCounter(listId: Int, ean: String, userId: String, value: Double) {
        try {
            shoppingListCloudService.updateProductQuantityOnList(listId, ean, userId, value)
        } catch (e: Exception) {
            throw e
        }
    }
*/

}