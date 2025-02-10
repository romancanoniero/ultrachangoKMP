package com.iyr.ultrachango.data.api.preciosclaros.models.sucursales

import kotlinx.serialization.Serializable


@Serializable
data class SucursalesAPI(
    val maxLimitPermitido: Int,
    val status: Int,
    val sucursales: ArrayList<Sucursale>,
    val total: Int,
    val totalPagina: Int
)