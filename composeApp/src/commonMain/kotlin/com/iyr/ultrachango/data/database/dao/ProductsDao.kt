package com.iyr.ultrachango.data.database.dao

/*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import androidx.room.Update
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListComplete
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.Stores
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductsDao {

    //------- PRODUCTS ----------------------

    @Query("SELECT * FROM products")
    fun fetchAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM products WHERE ean = :ean")
    fun findProductByEAN(ean: String): Flow<Product?>

    @Query("SELECT COUNT(ean) FROM products")
    suspend fun countProducts(): Int


    @Query("SELECT * FROM products WHERE name LIKE '%' || :text || '%' OR brand LIKE '%' || :text || '%' OR presentation LIKE '%' || :text || '%'")
    suspend fun searchByText(text: String): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    //----------------- SHOPPING LIST ----------------------------

    // TODO: 2021-09-29  Eliminar este metodo
    @Query("SELECT * FROM shopping_list ")
    fun fetchAllShoppingList(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list WHERE user_id = :userKey")
    fun fetchShoppingList(userKey: String): Flow<List<ShoppingList>>


    @Query("SELECT * FROM shopping_list  WHERE user_id = :userKey")
    suspend fun getShoppingLists(userKey: String): List<ShoppingList>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingList(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_list WHERE id = :shoppingListId")
    suspend fun getShoppingList(shoppingListId: Int?): ShoppingList

    @Query("SELECT COUNT(id) FROM shopping_list")
    suspend fun countShoppingList(): Int


    @Query("UPDATE shopping_list SET list_name = :newName WHERE id = :id")
    suspend fun renameList(id: Int, newName: String)

    //--------------


    ///----------------- SHOPPING LIST PRODUCTS ----------------------------
    /*
       @Query("SELECT * FROM shopping_list_products WHERE list_id = :listId")
       fun fetchListProducts(listId: Long) : Flow<List<ShoppingListItemModel>>
   */
    ///----------------------------------------------------------------------

/*
    @Transaction
    @Query("SELECT * FROM shopping_list WHERE id = :shoppingListId")
    suspend fun getShoppingListComplete(shoppingListId: Int): ShoppingListComplete
*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListProducts(products: List<ShoppingListProduct>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListMembers(members: List<ShoppingListMember>)

    @Transaction
    suspend fun insertShoppingList(
        shoppingList: ShoppingList,
        products: List<ShoppingListProduct>?,
        members: List<ShoppingListMember>?
    ) {
        try {
            val shoppingListId = insertShoppingList(shoppingList)

            products?.let { it ->
                if (it.isNotEmpty()) {
                    insertShoppingListProducts(products)
                }

            }

            members?.let { it ->
                insertShoppingListMembers(it)
            }
        } catch (
            e: Exception
        ) {
            throw e
        }


    }


    ///----------------- STORES ----------------------------

    @Query("SELECT * FROM stores ")
    fun fetchStores(): Flow<List<Stores>>


    @Query("SELECT * FROM stores ")
    suspend fun getStores(): List<Stores>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStores(store: List<Stores>)

    @Query("DELETE FROM shopping_list_products WHERE list_id = :id AND ean = :ean")
    suspend fun removeProductFromList(id: Int, ean: String)

    @Query("INSERT INTO shopping_list_products (list_id, ean) VALUES (:id, :ean)")
    suspend fun addProductToList(id: Int, ean: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateShoppingLists(toUpdate: List<ShoppingList>)

    @Query("DELETE FROM shopping_list WHERE id = :id")
    suspend fun deleteShoppingList(id: Int)

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toList: List<ShoppingList>)


}

*/