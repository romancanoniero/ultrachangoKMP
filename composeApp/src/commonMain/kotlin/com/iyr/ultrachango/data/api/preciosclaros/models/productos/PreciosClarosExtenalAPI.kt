package com.iyr.ultrachango.data.api.preciosclaros.models.productos


class PreciosClarosExtenalAPI {


    data class Comparativa(
        val status: Int,
        val sucursales: ArrayList<SucursalesAPI>,
        val totalProductos: Int,
        val totalSucursales: Int
    )

    data class SucursalesAPI (
        val actualizadoHoy: Boolean,
        val banderaDescripcion: String,
        val banderaId: Int,
        val comercioId: Int,
        val comercioRazonSocial: String,
        val direccion: String,
        val id: String,
        val lat: String,
        val lng: String,
        val localidad: String,
        val productos: List<ProductoAPI>,
        val provincia: String,
        val sucursalNombre: String,
        val sucursalTipo: String,
        val sumaPrecioListaDisponible: Int,
        val sumaPrecioListaTotal: Int
    )

    data class ProductoAPI(
        val id: String,
        val marca: String,
        val message: String,
        val nombre: String,
        val precioLista: Int,
        val presentacion: String,
        val promo1: PromoAPI,
        val promo2: PromoAPI
    )

    data class PromoAPI(
        val descripcion: String,
        val precio: String
    )
}