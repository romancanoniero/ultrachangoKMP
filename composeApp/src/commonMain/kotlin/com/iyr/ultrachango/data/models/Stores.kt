package com.iyr.ultrachango.data.models


import kotlinx.serialization.Serializable

@Serializable
/*
@Entity(tableName = "stores" , primaryKeys = ["banderaId", "comercioId", "id"])

 */
data class Stores (
    var actualizadoHoy: Boolean? = null,
    var banderaDescripcion: String? = null,
    var banderaId: Int ,
    var comercioId: Int,
    var comercioRazonSocial: String? = null,
    var direccion: String? = null,
    var id: String ,
    var lat: String? = null,
    var lng: String? = null,
    var localidad: String? = null,
    var provincia: String? = null,
    var sucursalId: String,
    var sucursalNombre: String? = null,
    var sucursalTipo: String? = null,
)
