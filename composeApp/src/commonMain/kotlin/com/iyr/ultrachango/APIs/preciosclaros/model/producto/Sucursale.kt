package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import com.iyr.ultrachango.data.models.PriceInBranch
import com.iyr.ultrachango.data.models.ProductPrice
import kotlinx.serialization.Serializable

@Serializable
data class Sucursale(
    val actualizadoHoy: Boolean,
    val banderaDescripcion: String,
    val banderaId: Int,
    val comercioId: Int,
    val comercioRazonSocial: String,
    val direccion: String,
    val distanciaDescripcion: String,
    val distanciaNumero: Double,
    val id: String,
    val lat: String,
    val lng: String,
    val localidad: String,
    val preciosProducto: Precio,
    val provincia: String,
    val sucursalNombre: String,
    val sucursalTipo: String
) {
    fun toPriceInBranch(): PriceInBranch {

        val price = ProductPrice(
            regularPrice = preciosProducto.getPrecioLista(),
            promo1 = if (!preciosProducto.promo1.toString().isNullOrEmpty())  preciosProducto.promo1?.toProductPromo() else null,

            promo2 = if (!preciosProducto.promo2.toString().isNullOrEmpty())  preciosProducto.promo2?.toProductPromo() else null,
        )


        return PriceInBranch(
            price =  price   ,
            distance = distanciaNumero.toFloat(),
            updatedToday = actualizadoHoy,
            uniqueKey = id,
            flagId = banderaId,
            flagDescription = banderaDescripcion,
            branchName = sucursalNombre,
            branchType = sucursalTipo,
            storeId = comercioId,
            storeBusinessName = comercioRazonSocial,
            address = direccion,
            id = id,
            latitude = lat,
            longitude = lng,
            locality = localidad,
            province = provincia,
            sumAvailableListPrice = 0,
            sumTotalListPrice = 0.00,
            gatheredProducts = 0,
            differenceWithOthers = 0
        )
    }
}