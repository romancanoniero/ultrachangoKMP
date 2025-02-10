package com.iyr.ultrachango.data.models

data class ComparissionsResults(
    var status: Int? = null,
    var sucursales: List<BranchForComparission>? = null,
    var totalProductos: Int? = null,
    var totalSucursales: Int? = null
)