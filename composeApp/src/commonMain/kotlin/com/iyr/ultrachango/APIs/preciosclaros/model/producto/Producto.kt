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


}


