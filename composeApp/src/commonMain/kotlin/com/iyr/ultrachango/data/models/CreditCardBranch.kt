package com.iyr.ultrachango.data.models


import com.iyr.ultrachango.ui.screens.fidelization.CardInfo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.amex_logo
import ultrachango2.composeapp.generated.resources.card_mesh
import ultrachango2.composeapp.generated.resources.card_mesh_2
import ultrachango2.composeapp.generated.resources.card_mesh_3
import ultrachango2.composeapp.generated.resources.discover_logo
import ultrachango2.composeapp.generated.resources.logo_citi
import ultrachango2.composeapp.generated.resources.logo_galicia
import ultrachango2.composeapp.generated.resources.logo_icbc
import ultrachango2.composeapp.generated.resources.logo_patagonia
import ultrachango2.composeapp.generated.resources.logo_santander
import ultrachango2.composeapp.generated.resources.mastercard_logo
import ultrachango2.composeapp.generated.resources.visa_logo




@Serializable
//@Entity(tableName = "credit_card_branch")
data class CreditCardBranch(
  //  @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Contextual
    val logoDrawable: DrawableResource,
)


val sampleCreditCardBranch  = listOf(
    CreditCardBranch(
        name = "Visa",
        logoDrawable = Res.drawable.visa_logo
    ),
    CreditCardBranch(
        name = "American Express",
        logoDrawable = Res.drawable.amex_logo
    ),
    CreditCardBranch(
        name = "MasterCard",
        logoDrawable = Res.drawable.mastercard_logo
    ),


)

