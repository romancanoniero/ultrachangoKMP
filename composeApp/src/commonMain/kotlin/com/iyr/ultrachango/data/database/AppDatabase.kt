package com.iyr.ultrachango.data.database

/*
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.iyr.ultrachango.data.database.dao.ProductsDao
import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ShoppingListProduct
import com.iyr.ultrachango.data.models.ShoppingListMember
import com.iyr.ultrachango.data.models.ShoppingList
import com.iyr.ultrachango.data.models.ShoppingListQuantities
import com.iyr.ultrachango.data.models.Stores
import com.iyr.ultrachango.data.models.User


const val DATABASE_NAME = "ultrachango.db"

interface DB {
    fun clearAllTables() {}
}

//ShoppingListItemModel::class

// Database class before the version update.
@ConstructedBy(AppDatabaseConstructor::class)
@Database(
    version = 1,
    entities = [
        Product::class,
        ShoppingList::class,
        ShoppingListProduct::class,
        User::class,
        ShoppingListMember::class,
        Stores::class]
)



@TypeConverters(ShoppingListItemTypeConverter::class)
//@ConstructedBy(AppDatabaseConstructor::class)
abstract class UltraChangoDatabase : RoomDatabase(), DB {
    abstract fun productsDao(): ProductsDao
    override fun clearAllTables() {}
}


// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<UltraChangoDatabase> {
    override fun initialize(): UltraChangoDatabase
}


fun getRoomDatabase(
    builder: RoomDatabase.Builder<UltraChangoDatabase>
): UltraChangoDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}


*/







