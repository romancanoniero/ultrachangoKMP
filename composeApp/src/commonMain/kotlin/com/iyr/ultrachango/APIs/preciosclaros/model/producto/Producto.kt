package com.iyr.ultrachango.data.Api.preciosclaros.model.producto

import com.iyr.ultrachango.data.models.Product
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: String,
    val marca: String? = null,
    val nombre: String? = null,
    val presentacion: String? = null
) {


    fun toDomainProduct(): Product {
        val originalObject = this
        return Product(
         //   id = originalObject.id.toLong(),
            ean = originalObject.id,
            brand = originalObject.marca,
            marca_lower = originalObject.marca.toString().lowercase(),
            nombre_lower = originalObject.nombre,
            name = originalObject.nombre.toString().lowercase(),
            presentation = originalObject.presentacion
        )
    }
}