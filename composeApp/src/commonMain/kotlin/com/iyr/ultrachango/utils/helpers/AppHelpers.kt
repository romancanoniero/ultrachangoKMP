package com.iyr.ultrachango.utils.helpers

import com.iyr.ultrachango.config.Config
import com.iyr.ultrachango.config.Config.BASE_URL_CLOUD_SERVER

fun getProfileImageURL(userKey: String, fileName: String?): String? {
    if (fileName == null) return null

    return "${Config.BASE_URL_CLOUD_SERVER}/api/client/${userKey}/image/${fileName}"
}

fun getProductImageUrl(ean: String): String {
    return "$BASE_URL_CLOUD_SERVER/products/images/ean/${ean}"

}

fun getBrandImageUrl(storeId : Int, flagId : Int): String {
//https://imagenes.preciosclaros.gob.ar/comercios/15-1.jpg
//    return "$BASE_URL_CLOUD_SERVER/comercios/${val1}-${val2}"
    return "https://imagenes.preciosclaros.gob.ar/comercios/${storeId}-${flagId}.jpg"

}




