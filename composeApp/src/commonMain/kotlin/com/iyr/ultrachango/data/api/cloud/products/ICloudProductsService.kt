package com.iyr.ultrachango.data.api.cloud.products

import com.iyr.ultrachango.data.models.Product
import com.iyr.ultrachango.data.models.ProductWithPricesAround

interface ICloudProductsService {

    suspend fun getProducts(): List<Product>

    suspend fun getProductById(id: String): Product

    suspend fun getProductByMarca(marca: String): List<Product>

    suspend fun getProductByText(text: String, latitude: Double, longitude: Double): List<Product>

    suspend fun getProductByEAN(ean: String): Product

    suspend fun getProductByEANWithShoppingList(ean: String, userId: String): HashMap<String, Any>

    suspend fun getProductByEANLatLng(ean: String,
                                      latitude: Double,
                                      longitude: Double,
                                      radius: Int): HashMap<String, Any>

    suspend fun createProduct(product: Product): Product

    suspend fun updateProduct(product: Product): Product

    suspend fun deleteProduct(id: String): Product


    fun togleProductFavorite(userKey: String, ean: String, favorite: Boolean): Product

    suspend fun getSuggestedProductsWithDetailed(
        text: String,
        latitude: Double,
        longitude: Double,
        distance : Double
    ): List<ProductWithPricesAround>?

   }