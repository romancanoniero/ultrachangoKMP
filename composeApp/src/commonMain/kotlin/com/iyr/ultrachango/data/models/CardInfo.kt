package com.iyr.ultrachango.data.models


import com.iyr.ultrachango.ui.screens.fidelization.CardInfo
import kotlinx.serialization.Serializable
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.amex_logo
import ultrachango2.composeapp.generated.resources.card_mesh
import ultrachango2.composeapp.generated.resources.card_mesh_2
import ultrachango2.composeapp.generated.resources.card_mesh_3
import ultrachango2.composeapp.generated.resources.discover_logo
import ultrachango2.composeapp.generated.resources.mastercard_logo
import ultrachango2.composeapp.generated.resources.visa_logo

@Serializable
//@Entity(tableName = "cards")
data class CardInfo(
 //   @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardNumber: String,
    val cardHolder: String,
    val providerDrawable: Int,
    val backgroundDrawable: Int )



val sampleCards = listOf(
    CardInfo("1234 5678 9012 3456",
        "John Doe",
        Res.drawable.visa_logo,
        Res.drawable.card_mesh),
    CardInfo("2345 6789 0123 4567", "Jane Smith", Res.drawable.mastercard_logo, Res.drawable.card_mesh_2),
    CardInfo("3456 7890 1234 5678", "Alice Johnson", Res.drawable.amex_logo, Res.drawable.card_mesh_3),
    CardInfo("4567 8901 2345 6789", "Bob Brown", Res.drawable.discover_logo, Res.drawable.card_mesh),
    CardInfo("5678 9012 3456 7890", "Charlie Davis", Res.drawable.visa_logo, Res.drawable.card_mesh_2)
)
