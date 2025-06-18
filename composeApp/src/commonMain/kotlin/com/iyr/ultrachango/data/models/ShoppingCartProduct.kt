package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable

@Serializable
data class ShoppingCartProduct(
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
    override var haveImage: Boolean? = false,
    var status: String? = null,
    var requirers: ArrayList<UserMinimum>? = null,
    var qty: Double? = null,
    var productPrice: ProductPrice? = null
) : ProductBase {
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



val sampleShoppingCartProducts = listOf(
    ShoppingCartProduct(
        ean = "1234567890123",
        name = "Product 1",
        brand = "Brand A",
        presentation = "Box",
        status = "Available",
        haveImage = true,
        requirers = arrayListOf(UserMinimum("User1"), UserMinimum("User2")),
        qty = 1.0
    ),
    ShoppingCartProduct(
        ean = "1234567890124",
        name = "Product 2",
        brand = "Brand B",
        presentation = "Bottle",
        status = "Available",
        haveImage = false,
        requirers = arrayListOf(UserMinimum("User3")),
        qty = 2.0
    ),
    ShoppingCartProduct(
        ean = "1234567890125",
        name = "Product 3",
        brand = "Brand C",
        presentation = "Pack",
        status = "Out of Stock",
        haveImage = true,
        requirers = arrayListOf(UserMinimum("User4"), UserMinimum("User5")),
        qty = 3.0
    ),
    ShoppingCartProduct(
        ean = "1234567890126",
        name = "Product 4",
        brand = "Brand D",
        presentation = "Can",
        status = "Available",
        haveImage = false,
        requirers = arrayListOf(UserMinimum("User6")),
        qty = 4.0
    ),
    ShoppingCartProduct(
        ean = "1234567890127",
        name = "Product 5",
        brand = "Brand E",
        presentation = "Bag",
        status = "Available",
        haveImage = true,
        requirers = arrayListOf(UserMinimum("User7"), UserMinimum("User8")),
        qty = 5.0
    ),
    ShoppingCartProduct(
        ean = "1234567890128",
        name = "Product 6",
        brand = "Brand F",
        presentation = "Box",
        status = "Out of Stock",
        haveImage = false,
        requirers = arrayListOf(UserMinimum("User9")),
        qty = 6.0
    ),
    ShoppingCartProduct(
        ean = "1234567890129",
        name = "Product 7",
        brand = "Brand G",
        presentation = "Bottle",
        status = "Available",
        haveImage = true,
        requirers = arrayListOf(UserMinimum("User10")),
        qty = 7.0
    ),
    ShoppingCartProduct(
        ean = "1234567890130",
        name = "Product 8",
        brand = "Brand H",
        presentation = "Pack",
        status = "Available",
        haveImage = false,
        requirers = arrayListOf(UserMinimum("User11"), UserMinimum("User12")),
        qty = 8.0
    ),
    ShoppingCartProduct(
        ean = "1234567890131",
        name = "Product 9",
        brand = "Brand I",
        presentation = "Can",
        status = "Out of Stock",
        haveImage = true,
        requirers = arrayListOf(UserMinimum("User13")),
        qty = 9.0
    ),
    ShoppingCartProduct(
        ean = "1234567890132",
        name = "Product 10",
        brand = "Brand J",
        presentation = "Bag",
        status = "Available",
        haveImage = false,
        requirers = arrayListOf(UserMinimum("User14"), UserMinimum("User15")),
        qty = 10.0
    )
)
