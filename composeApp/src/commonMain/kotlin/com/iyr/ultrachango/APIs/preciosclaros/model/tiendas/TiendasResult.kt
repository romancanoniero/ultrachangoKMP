package com.iyr.shoppingorganizer.dao.models

import com.iyr.ultrachango.data.models.ProductForComparission

data class TiendasResult(
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
)