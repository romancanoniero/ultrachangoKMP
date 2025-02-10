package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import com.iyr.ultrachango.data.models.Product
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProductResult(
    val maxLimitPermitido: Int? = null,
    val producto: Producto,
    val status: Int,
    val sucursales: List<Sucursale>,
    val total: Int,
    val totalPagina: Int? = null
) {
    fun toProduct(): Product {
        return Product(
            ean = producto.id,
            name = producto.nombre,
            brand = producto.marca,

        )

    }
}