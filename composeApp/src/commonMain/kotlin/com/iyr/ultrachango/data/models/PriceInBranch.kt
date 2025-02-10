package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable


@Serializable
//@Entity("product_prices")
data class PriceInBranch(
    val distance: Float = 0.0f,
    val updatedToday: Boolean? = null,
    val uniqueKey: String? = null,
    val flagId: Int? = null,
    val flagDescription: String? = null,
    val branchName: String? = null,
    val branchType: String? = null,
    val storeId: Int? = null,
    val storeBusinessName: String? = null,
    val address: String? = null,
    val id: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val locality: String? = null,
  //  val products: ArrayList<ProductForComparission>? = null,
    val price : ProductPrice? = null,
    val province: String? = null,

    val sumAvailableListPrice: Int? = null,
    val sumTotalListPrice: Double? = null,
    val gatheredProducts: Int = 0,
    val differenceWithOthers: Int = 0
)

/*

    var distancia: Float = 0.0f,
    var actualizadoHoy: Boolean? = null,
    var uniqueKey: String? = null,
    var banderaDescripcion: String? = null,
    var banderaId: Int? = null,
    var comercioId: Int? = null,
    var comercioRazonSocial: String? = null,
    var direccion: String? = null,
    var id: String? = null,
    var lat: String? = null,
    var lng: String? = null,
    var localidad: String? = null,
    var productos: ArrayList<ProductForComparission>? = null,
    var provincia: String? = null,
    var sucursalNombre: String? = null,
    var sucursalTipo: String? = null,
    var sumaPrecioListaDisponible: Int? = null,
    var sumaPrecioListaTotal: Double? = null,
    var productosReunidos: Int = 0,
    var diferenciaConOtros: Int = 0

 */