package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.data.api.preciosclaros.PreciosClarosService



class StoresRepository(
    private val tiendasService: PreciosClarosService,
  //  private val productsDao: ProductsDao,
) {
/*
    suspend fun getStoresList(
        lat: Double, lng: Double, radius: Double, limit: Int?
    ): List<Stores> {
        val localCall = productsDao.getStores()
        if (localCall.isEmpty()) {
            val remoteMovies =
                tiendasService.getStoresList(lat, lng, radius, limit)?.let { result ->
                    result.map { it.toDomainStore() }
                        .filter { remoteStore -> localCall.none { it.id == remoteStore.id } }
                }
            remoteMovies?.let { newData ->
                productsDao.saveStores(newData)
            }
        }
        return productsDao.getStores()
    }
*/
}