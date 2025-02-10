package com.iyr.ultrachango.data.database.dao


/*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingList
import kotlinx.coroutines.flow.Flow


@Dao
interface ShoppingListDao2 {

    @Query("SELECT * FROM products")
    fun fetchAllPipis(): Flow<List<Product>>

    @Query("SELECT * FROM shopping_list")
    fun fetchAllProducts(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list")
    suspend fun getProducts(): List<ShoppingList>


    @Query("SELECT * FROM shopping_list WHERE id = :id")
    fun findProductById(id: Int): Flow<ShoppingList?>

    @Query("SELECT COUNT(id) FROM shopping_list")
    suspend fun countProducts(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ShoppingList>)
}

*/