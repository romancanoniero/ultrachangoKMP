package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable


@Serializable
//@Entity("products")
data class ShoppingCartGeneric
    (
    var ean: String? = null,

    //@Transient
    var name: String? = null,

    var haveImage: Boolean = false,
  //  @Transient
    var requirers: ArrayList<UserMinimum>? = null,

    var qty: Double? = null,
)  {
    fun toShoppingCartProduct(): ShoppingCartProduct {
        return ShoppingCartProduct(
            ean = this.ean,
            name = this.ean,
            haveImage = false,
            requirers = this.requirers,
            qty = this.qty
        )
    }

}

