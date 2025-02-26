package com.iyr.ultrachango.data.database.repositories

import com.iyr.ultrachango.data.api.cloud.familymembers.CloudFamilyMembersService


import com.iyr.ultrachango.data.models.FamilyMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FamilyMembersRepository(
  //  private val shoppingListDao: ProductsDao,
    private val familyMembersCloudService: CloudFamilyMembersService,
) {

    fun getList(userKey: String): Flow<List<FamilyMember>> = flow {

        val cloudCall = familyMembersCloudService.getList(userKey)
        emit(cloudCall)
    }


}