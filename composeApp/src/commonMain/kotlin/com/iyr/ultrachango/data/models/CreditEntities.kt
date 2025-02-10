package com.iyr.ultrachango.data.models


import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.logo_axion_on
import ultrachango2.composeapp.generated.resources.logo_citi
import ultrachango2.composeapp.generated.resources.logo_galicia
import ultrachango2.composeapp.generated.resources.logo_icbc
import ultrachango2.composeapp.generated.resources.logo_modo
import ultrachango2.composeapp.generated.resources.logo_patagonia
import ultrachango2.composeapp.generated.resources.logo_santander
import ultrachango2.composeapp.generated.resources.logo_uala


enum class CreditEntityType {
    BANK,
    REWARDS
}


@Serializable
//@Entity(tableName = "credit_entities")
data class CreditEntities(
  //  @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: CreditEntityType,
    val name: String,
    @Contextual
    val logoDrawable: DrawableResource,
)


val sampleCreditEntities = listOf(
    CreditEntities(
        type = CreditEntityType.REWARDS,
        name = "Modo",
        logoDrawable = Res.drawable.logo_modo
    ),
    CreditEntities(
        type = CreditEntityType.REWARDS,
        name = "Uala",
        logoDrawable = Res.drawable.logo_uala
    ),
    CreditEntities(
        type = CreditEntityType.REWARDS,
        name = "Uala",
        logoDrawable = Res.drawable.logo_axion_on
    ),
    CreditEntities(
        type = CreditEntityType.BANK,
        name = "Santander",
        logoDrawable = Res.drawable.logo_santander
    ),
    CreditEntities(
        type = CreditEntityType.BANK,
        name = "Galicia",
        logoDrawable = Res.drawable.logo_galicia
    )
    ,
    CreditEntities(
        type = CreditEntityType.BANK,
        name = "Citi",
        logoDrawable = Res.drawable.logo_citi
    )
    ,
    CreditEntities(
        type = CreditEntityType.BANK,
        name = "Patagonia",
        logoDrawable = Res.drawable.logo_patagonia
    )
    ,
    CreditEntities(
        type = CreditEntityType.BANK,
        name = "ICBC",
        logoDrawable = Res.drawable.logo_icbc
    )

)

