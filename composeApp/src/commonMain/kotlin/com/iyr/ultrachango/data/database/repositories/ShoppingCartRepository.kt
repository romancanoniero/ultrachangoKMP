package com.iyr.ultrachango.data.database.repositories


import com.iyr.ultrachango.data.api.cloud.buy.prepare.CloudShoppingCartService
import com.iyr.ultrachango.data.models.ShoppingCart
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.utils.auth_by_cursor.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ShoppingCartRepository(
    // private val shoppingListDao: ProductsDao,
    private val authRepository: AuthRepository,
    private val shoppingCartCloudService: CloudShoppingCartService,
) {

    fun fetchLists(): Flow<List<ShoppingCart>> = flow {
        val userKey = authRepository.getUserKey()
        userKey.let {
            userKey!!
            val cloudCall = shoppingCartCloudService.getAll(userKey)

            emit(cloudCall)
        }
    }


    suspend fun list(): List<ShoppingCart> {
        val userKey = authRepository.getUserKey()
        userKey?.let {
            val cloudCall = shoppingCartCloudService.getAll(userKey)
            return cloudCall
        } ?: throw Exception("User not found")
    }


    private fun mapToShoppingList(shoppingListComplete: List<ShoppingListComplete>): List<ShoppingList> {

        val newList: ArrayList<ShoppingList> = ArrayList<ShoppingList>()
        shoppingListComplete.forEach {
            val shoppingList = ShoppingList(
                listId = it.listId,
                userId = it.userKey,
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

    suspend fun saveShoppingList(shoppingCart: ShoppingCart): ShoppingCart? {
        val userKey = shoppingCart.userKey.toString()
        return try {
            shoppingCartCloudService.save(shoppingCart)?.let { newEntity ->
                return@let newEntity
            }

        } catch (e: Exception) {
            throw e
        }
    }
/*
    suspend fun renameList(shoppingListId: Int, newName: String) {
        //     shoppingListDao.renameList(shoppingListId, newName)

        val userKey = authRepository.getUserKey()
        try {
            userKey?.let { userKey ->
                preparationListCloudService.get(userKey, shoppingListId).let { entityCompleted ->
                    val entity = entityCompleted.toShoppingList()
                    entity.listName = newName

                    preparationListCloudService.save(entity)

                }
            } ?: throw Exception("User not found")
        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }


    }
*/

    suspend fun removeList(shoppingList: ShoppingList) {
        try {
            shoppingCartCloudService.delete(shoppingList.userId.toString(), shoppingList.listId!!)
        } catch (e: Exception) {
            // si falla la nube, elimino el registro local
            //   shoppingListDao.deleteShoppingList(shoppingList.id)
            throw e
        }

    }

    suspend fun getShoppingCart(shoppingListId: Int): ShoppingCart {
        val userKey = authRepository.getUserKey()
        userKey?.let {
            return shoppingCartCloudService.get(userKey, shoppingListId)
        }?: throw Exception("User not found")
    }

    suspend fun countShoppingList(): Int = shoppingCartCloudService.count()


    suspend fun removeProductFromList(id: Int, ean: String) {
        //  shoppingListDao.removeProductFromList(id, ean)
        shoppingCartCloudService.removeProductFromShoppingList(id, ean)
    }

    suspend fun addProductToList(id: Int, ean: String, quantity: Double) {
        try {
            val token = authRepository.getAuthToken(false)!!
            val userKey = authRepository.getUserKey()
            userKey?.let {
                shoppingCartCloudService.addProductToList(token, id, ean, userKey, quantity)
            } ?: throw Exception("User not found")

            //shoppingListDao.addProductToList(id, ean)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateProductCounter( listId: Int, ean: String, userId: String, value: Double) {
        try {
            val token = authRepository.getAuthToken(false)!!
            shoppingCartCloudService.updateProductQuantityOnList(
               token = token,
                listId = listId,
                ean = ean,
                userKey = userId,
                quantity = value)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getMembers(listId: Long): List<ShoppingListMember> {
        return shoppingCartCloudService.getMembers(listId)
    }


    /**
     * Get the members of a shopping list and completes the list with the family Members of the user
     */

    fun getMembers(listId: Long, userId: String): Flow<List<ShoppingListMember>> = flow {
        val sorted = shoppingCartCloudService.getMembers(listId, userId)
            .sortedWith(compareByDescending<ShoppingListMember> { it.isAdmin }.thenBy { it.user?.displayName })

        emit(sorted)
    }

    suspend fun addMember(listId: Long, userId: String) {

        try {
            shoppingCartCloudService.addMember(listId, userId)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun removeMember(listId: Long, userId: String) {
        try {
            return shoppingCartCloudService.removeMember(listId, userId)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun createShoppingCart(): ShoppingCart {
        try {
            val userKey = authRepository.getUserKey().toString()
            return shoppingCartCloudService.createEmptyCart(userKey)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun update(updatedShoppingCart: ShoppingCart) {
        try {
            val userKey = authRepository.getUserKey().toString()
            if (updatedShoppingCart.userKey.isNullOrBlank() )
            {
                updatedShoppingCart.userKey = userKey
            }
            val token = authRepository.getAuthToken(false)!!
            return shoppingCartCloudService.update(token = token, shoppingCart = updatedShoppingCart)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun getOrCreateShoppingCart(): ShoppingCart {
        try {
            val userKey = authRepository.getUserKey().toString()
            val token = authRepository.getAuthToken(false)!!
            return shoppingCartCloudService.getOrCreateShoppingCart(token = token, userKey = userKey)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun clear(): Boolean {
        try {
            val userKey = authRepository.getUserKey().toString()
            val token = authRepository.getAuthToken(false)!!
            return shoppingCartCloudService.emptyTrash(token = token, userKey = userKey)
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun setPriceToProduct(shoppingCartId: Int?, ean: String, posKey: String) {
        try {
            val userKey = authRepository.getUserKey().toString()
            val token = authRepository.getAuthToken(false)!!
            return shoppingCartCloudService.setPriceToProduct(
                token = token,
                shoppingCartId = shoppingCartId!!,
                ean = ean,
                posKey = posKey
                )
        } catch (ex: Exception) {
            throw ex
        }

    }
}