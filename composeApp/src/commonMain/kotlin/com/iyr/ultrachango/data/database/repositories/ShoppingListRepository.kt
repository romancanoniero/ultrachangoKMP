package com.iyr.ultrachango.data.database.repositories


import com.iyr.ultrachango.auth.AuthRepository
import com.iyr.ultrachango.data.api.cloud.shoppinglist.CloudShoppingListService
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.data.models.ShoppingListProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ShoppingListRepository(
    // private val shoppingListDao: ProductsDao,
    private val authRepository: AuthRepository,
    private val shoppingListCloudService: CloudShoppingListService,
) {

    fun fetchLists(): Flow<List<ShoppingList>> = flow {
        val userKey = authRepository.getUserKey()
        userKey.let {
            val cloudCall = shoppingListCloudService.getAll(userKey)
            val toShoppingList = mapToShoppingList(cloudCall)
            emit(toShoppingList)
        }
    }


    suspend fun list(): List<ShoppingListComplete> {
        val userKey = authRepository.getUserKey()
        val cloudCall = shoppingListCloudService.getAll(userKey)
        return cloudCall
    }


    private fun mapToShoppingList(shoppingListComplete: List<ShoppingListComplete>): List<ShoppingList> {

        val newList: ArrayList<ShoppingList> = ArrayList<ShoppingList>()
        shoppingListComplete.forEach {
            val shoppingList = ShoppingList(listId = it.listId,
                userId = it.userId,
                listName = it.listName,
                imageUrl = it.imageUrl,
                items = it.items?.map { productComplete ->
                    ShoppingListProduct(
                        it.listId!!,
                        productComplete.product?.ean!!,
                        productComplete.quantities
                    )
                } ?: emptyList(),
                members = it.members,
/*
                members = it.members?.map { memberComplete ->
                    ShoppingListMember(it.listId?.toInt()!!, memberComplete.userId)
                } ?: emptyList(),
*/
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

    suspend fun saveShoppingList(shoppingList: ShoppingList): ShoppingList? {
        val userKey = shoppingList.userId.toString()
        return try {
            shoppingListCloudService.save(shoppingList)?.let { newEntity ->
                return@let newEntity
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun renameList(shoppingListId: Int, newName: String) {
        //     shoppingListDao.renameList(shoppingListId, newName)

        val userKey = authRepository.getUserKey()
        try {

            shoppingListCloudService.get(userKey, shoppingListId).let { entityCompleted ->
                val entity = entityCompleted.toShoppingList()
                entity.listName = newName

                shoppingListCloudService.save(entity)

            }

        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }


    }


    suspend fun removeList(shoppingList: ShoppingList) {
        try {
            shoppingListCloudService.delete(shoppingList.userId.toString(), shoppingList.listId!!)
        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }

    }

    suspend fun getShoppingList(shoppingListId: Int): ShoppingListComplete {
        val userKey = authRepository.getUserKey()
        return shoppingListCloudService.get(userKey, shoppingListId)
    }

    suspend fun countShoppingList(): Int = shoppingListCloudService.count()


    suspend fun removeProductFromList(id: Int, ean: String) {
        //  shoppingListDao.removeProductFromList(id, ean)
        shoppingListCloudService.removeProductFromShoppingList(id, ean)
    }

    suspend fun addProductToList(id: Int, ean: String, quantity: Int) {
        try {
            val userKey = authRepository.getUserKey()
            shoppingListCloudService.addProductToList(id, ean, userKey, quantity)

            //shoppingListDao.addProductToList(id, ean)
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

    suspend fun getMembers(listId: Long): List<ShoppingListMember> {
        return shoppingListCloudService.getMembers(listId)
    }


    /**
     * Get the members of a shopping list and completes the list with the family Members of the user
     */

    fun getMembers(listId: Long, userId: String): Flow<List<ShoppingListMember>> = flow {
        val sorted = shoppingListCloudService.getMembers(listId, userId)
            .sortedWith(compareByDescending<ShoppingListMember> { it.isAdmin }.thenBy { it.user?.nick })

        emit(sorted)
    }

    suspend fun addMember(listId: Long, userId: String) {

        try {
            shoppingListCloudService.addMember(listId, userId)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun removeMember(listId: Long, userId: String) {
        try {
            return shoppingListCloudService.removeMember(listId, userId)
        } catch (ex: Exception) {
            throw ex
        }
    }
}