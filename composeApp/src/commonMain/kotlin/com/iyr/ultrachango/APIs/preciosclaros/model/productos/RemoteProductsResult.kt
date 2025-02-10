package com.iyr.ultrachango.data.Api.preciosclaros.model.productos


import kotlinx.serialization.Serializable

@Serializable
data class RemoteProductsResult(
    val maxCantSucursalesPermitido: Int? = null,
    val maxLimitPermitido: Int? = null,
    val productos: ArrayList<Producto>? = null,
    val status: Int,
    val total: Int? = null,
    val totalPagina: Int?   = null,
    val errorDescription : String? = null,
    val errorType : String? = null,
    val errorCode : Int? = null
)

