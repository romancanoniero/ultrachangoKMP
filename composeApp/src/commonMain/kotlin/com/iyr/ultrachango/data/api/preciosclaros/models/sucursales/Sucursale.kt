package com.iyr.ultrachango.data.api.preciosclaros.models.sucursales

import com.iyr.ultrachango.data.models.Stores
import kotlinx.serialization.Serializable

@Serializable
data class Sucursale(
    val banderaDescripcion: String,
    val banderaId: Int,
    val comercioId: Int,
    val comercioRazonSocial: String,
    val direccion: String,
    val distanciaDescripcion: String,
    val distanciaNumero: Double,
    val id: String,
    val lat: String,
    val lng: String,
    val localidad: String,
    val provincia: String,
    val sucursalId: String,
    val sucursalNombre: String,
    val sucursalTipo: String
) {

    fun toDomainStore(): Stores {
        return Stores(
            banderaDescripcion = this.banderaDescripcion,
            banderaId = this.banderaId,
            comercioId = this.comercioId,
            comercioRazonSocial = this.comercioRazonSocial,
            direccion = this.direccion,
            id = this.id,
            lat = this.lat,
            lng = this.lng,
            localidad = this.localidad,
            provincia = this.provincia,
            sucursalId = this.sucursalId,
            sucursalNombre = this.sucursalNombre,
            sucursalTipo = this.sucursalTipo
        )
    }

}