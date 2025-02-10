package com.iyr.ultrachango.data.Api.preciosclaros.model.productos


import com.iyr.ultrachango.data.models.Product
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val cantSucursalesDisponible: Int,
    val id: String,
    val marca: String? = "",
    val nombre: String,
    val precioMax: Double? = null,
    val precioMin: Double? = null,
    val presentacion: String
) {
    fun toDomainProduct(): Product {

        return Product(
       //     id = id.toString().toLong() ,
            ean = id,
            brand = marca,
            name = nombre,
            presentation = presentacion
        )
    }
}