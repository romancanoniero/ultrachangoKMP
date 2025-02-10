package com.iyr.ultrachango.data.database

/*
import androidx.room.TypeConverter
import com.iyr.ultrachango.data.models.ShoppingListProduct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual class ShoppingListItemTypeConverter {
    @TypeConverter
    actual fun fromListToJson(list: ArrayList<ShoppingListProduct>?): String? {
        return Json.encodeToString(list)
    }

    @TypeConverter
    actual fun fromJsonToList(json: String?): ArrayList<ShoppingListProduct>? {
     //   val listType = object : TypeToken<ArrayList<ShoppingListItem>>() {}.type
        return json?.let { Json.decodeFromString(it) }
    }
}
*/