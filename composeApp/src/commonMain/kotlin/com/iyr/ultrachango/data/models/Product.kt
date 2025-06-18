package com.iyr.ultrachango.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

interface ProductBase {
    var ean: String?
    var name: String?
    var brand: String?
    var description: String?
    var presentationUnit: String?
    var presentationQty: Double?
    var marca_lower: String?
    var message: String?
    var nombre_lower: String?
    var presentation: String?
    var haveImage: Boolean?
}

// Product.kt
@Serializable
class Product__(
    var ean: String? = null,
    var name: String? = null,
    var brand: String? = null,
    var description: String? = null,
    var presentationUnit: String? = null,
    var presentationQty: Double? = null,
    var marca_lower: String? = null,
    var message: String? = null,
    var nombre_lower: String? = null,
    var presentation: String? = null,
    var haveImage: Boolean? = false,
)

@Serializable
class Product (
    override var ean: String? = null,
    override var name: String? = null,
    override var brand: String? = null,
    override var description: String? = null,
    override var presentationUnit: String? = null,
    override var presentationQty: Double? = null,
    override var marca_lower: String? = null,
    override var message: String? = null,
    override var nombre_lower: String? = null,
    override var presentation: String? = null,
    override var haveImage: Boolean? = null
) : ProductBase

// Implementación base para cuando necesitemos instanciar Product directamente
/*
@Serializable
class BaseProduct(
    var ean: String? = null,
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
)
*/

/*
@Serializable
data class ShoppingCartProduct(
    override var ean: String? = null,
    override var name: String? = null,
    override var brand: String? = null,
    override var presentation: String? = null,
    override var haveImage: Boolean? = false,
    var status: String? = null,
    var requirers: ArrayList<UserMinimum>? = null,
    var qty: Double? = null,
    var productPrice: ProductPrice? = null,
    // Estos campos los ponemos como null ya que no son necesarios en ShoppingCartProduct
    override var description: String? = null,
    override var presentationUnit: String? = null,
    override var presentationQty: Double? = null,
    override var marca_lower: String? = null,
    override var message: String? = null,
    override var nombre_lower: String? = null
) : Product {
    fun toProductGeneric(): ShoppingCartGeneric {
        return ShoppingCartGeneric(
            ean = ean,
            name = ean,
            haveImage = false,
            requirers = requirers,
            qty = 1.0
        )
    }
}
*/

fun Product.toProductOnSearch(): ProductOnSearch {
    return ProductOnSearch(
        ean = this.ean,
        name = this.name,
        brand = this.brand,
        presentation = this.presentation,
        haveImage = this.haveImage ?: false

    )
}

//-------------

/*
@Serializable
//@Entity("products")
open class Product
    (
   // @PrimaryKey
    var ean: String? = null,
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

//@Serializable
class ProductOnSearch(

    ean: String? = null,
    name: String? = null,
    brand: String? = null,
    presentation: String? = null,
    var status: String? = null,
    haveImage: Boolean = false
) : Product(
    ean = ean,
    name = name,
    brand = brand,
    presentation = presentation,
    haveImage = haveImage
) {
    fun toProduct(): Product = this
}


data class ProductOnSearch
    (
    var ean: String? = null,
    var name: String? = null,
    var brand: String? = null,
    var presentation: String? = null,
    var status: String? = null,
    var haveImage: Boolean = false
)  {
    fun toProduct(): Product {
        return Product(
            ean = this.ean,
            name = this.name,
            brand = this.brand,
            presentation = this.presentation
        )
    }
}
*/

