package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable




@Serializable
//@Entity("products")
data class Product
    (
   // @PrimaryKey
    var ean: String,
    var name: String? = null,
    var brand: String? = null,
    var description: String? = null,
    var presentationUnit: String? = null,
    var presentationQty: Double? = null,
    var marca_lower: String? = null,
    var message: String? = null,
    var nombre_lower: String? = null,
    var presentation: String? = null,
    var haveImage: Boolean? = false

) {
    constructor() : this(
        ean = "",
        brand = "",
        marca_lower = "",
        message = "",
        name = "",
        nombre_lower = "",
        presentation = ""
    )

    fun toProductOnSearch(): ProductOnSearch {
        return ProductOnSearch(
            ean = this.ean,
            name = this.name,
            brand = this.brand,
            presentation = this.presentation,
            haveImage = this.haveImage ?: false

        )
    }
}

data class ProductOnSearch
    (
    var ean: String,
    var name: String? = null,
    var brand: String? = null,
    var presentation: String? = null,
    var status: String? = null,
    var haveImage: Boolean = false
) {
    fun toProduct(): Product {
        return Product(
            ean = this.ean,
            name = this.name,
            brand = this.brand,
            presentation = this.presentation
        )
    }
}


