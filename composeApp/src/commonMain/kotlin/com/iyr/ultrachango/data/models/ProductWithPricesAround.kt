package com.iyr.ultrachango.data.models

import com.iyr.ultrachango.data.Api.preciosclaros.model.producto.Promo
import kotlinx.serialization.Serializable

@Serializable
data class ProductWithPricesAround(
    override var ean: String? = null,
    override var name: String? = null,
    override var brand: String? = null,
    override var presentation: String? = null,
    override var haveImage: Boolean? = false,
    var status: String? = null,
    // Estos campos los ponemos como null ya que no son necesarios en ProductOnSearch
    override var description: String? = null,
    override var presentationUnit: String? = null,
    override var presentationQty: Double? = null,
    override var marca_lower: String? = null,
    override var message: String? = null,
    override var nombre_lower: String? = null,
    var preciosTiendas: List<PreciosTienda>? = null,
) : ProductBase

//: Product
@Serializable
data class PreciosTienda(
    val address: String,
    val branchId: String,
    val branchName: String,
    val brandId: Int,
    val brandName: String,
    val creationTime: Int,
    val formatId: Int,
    val formatName: String,
    val id: String,
    val lat: String,
    val lng: String,
    val localty: String,
    val precio: Double?,
    val precioPromo1: Promo?,
    val precioPromo2: Promo?,
    val state: String,
)





