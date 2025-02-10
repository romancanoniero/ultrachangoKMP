package com.iyr.ultrachango.ui.screens.fidelization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iyr.ultrachango.data.models.CreditEntities
import com.iyr.ultrachango.data.models.sampleCreditEntities
import com.iyr.ultrachango.ui.dialogs.InfoDialog
import com.iyr.ultrachango.ui.theme.textColor
import com.iyr.ultrachango.utils.ui.elements.buttonShapeMedium
import com.iyr.ultrachango.utils.ui.elements.screenOuterPadding
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FidelizationScreen(navController: NavController) {


    var currentItems by remember { mutableStateOf(listOf<CreditEntities>()) }

    var showInformationDialog by remember { mutableStateOf(true) }
    var showSelectionDialog by remember { mutableStateOf(false) }


    if (showInformationDialog) {

        InfoDialog(title = "Tarjetas de Fidelizacion",
            paragraoh1 = "A la hora de ahorrar todas las ayudas son buenas. Existen Bancos, Empresas de Tarjetas de Credito y otros instrumentos financieros que brindan descuentos a sus clientes los cuales pueden ayudarte mucho al momento de planificar tus compras",
            paragraoh2 = "Por eso, te sera util que selecciones las tarjetas o programas a los que estes subscripto para optimizar el proceso de analisis de compras",
            onCancelPressed = {
                showInformationDialog = false
            }

        )

    } else
        if (showSelectionDialog) {
            FidelizationSelectionDialog(creditEntities = sampleCreditEntities.filter { it !in currentItems },
                onAcceptPressed = { selectedItems ->
                    // Handle accept action with selectedItems

                    currentItems = currentItems + selectedItems.filter { it !in currentItems }
                    showSelectionDialog = false
                },
                onCancelPressed = {
                    // Handle cancel action
                    showSelectionDialog = false
                })
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(screenOuterPadding),

        horizontalAlignment = Alignment.CenterHorizontally
    )

    {


        //LazyVerticalStaggeredGrid()
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(count = 3),
            modifier = Modifier.weight(1f).padding(vertical = 10.dp)
        ) {
            items(currentItems) { entity ->
                //CreditCard(card)
                CreditEntitiesCard(
                    entity = entity, onClick = {}, cardSize = CardsSize.SMALL
                )
            }

        }


        Button(modifier = Modifier.fillMaxWidth().background(Color.Black, buttonShapeMedium)
            .padding(2.dp),

            colors = androidx.compose.material.ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black, contentColor = Color.White
            ),

            onClick = {
                triggerHapticFeedback()
                showSelectionDialog = true
            }, content = {
                Text(
                    color = Color.White, text = "Agregar Tarjeta"
                )
            })
    }


}


@Composable
fun CreditCard(cardInfo: CardInfo) {
    Card(
        modifier = Modifier.height(200.dp).aspectRatio(16f / 9f),
        colors = CardColors(
            containerColor = Color.LightGray,
            contentColor = textColor,
            disabledContainerColor = Color.Blue,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),


        ) {
/*
        Image(
            painter = painterResource(resource = Res.drawable.card_mesh),
            contentDescription = "Card Background",
            contentScale = ContentScale.FillBounds
        )

 */
    }
}

enum class CardsSize {
    SMALL, MEDIUM, LARGE
}


@Composable
fun CreditEntitiesCard(
    cardSize: CardsSize? = CardsSize.MEDIUM,
    entity: CreditEntities,
    selecteds: List<CreditEntities>? = null,
    onClick: () -> Unit,

    ) {


    Column {
        Card(
            modifier = Modifier.aspectRatio(1f / 1f).padding(4.dp)
                .height(getCardSize(cardSize))
                .clickable { onClick() },
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = textColor,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(),
            shape = RoundedCornerShape(12.dp),
        ) {

            Box()
            {
                Image(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    painter = painterResource(resource = entity.logoDrawable),
                    contentDescription = "Card Background",
                    contentScale = ContentScale.Fit
                )

                if (selecteds?.contains(entity) == true) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                    )
                    {
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .aspectRatio(1f / 1f)
                                .background(Color.Blue.copy(alpha = 0.5f), CircleShape)
                                .align(Alignment.BottomStart)
                        )

                        Icon(
                            modifier = Modifier
                                .height(40.dp)
                                .aspectRatio(1f / 1f),
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                }
            }

        }

        Text(text = entity.name)
    }

}

fun getCardSize(cardSize: CardsSize?): Dp {
    return when (cardSize!!) {
        CardsSize.SMALL -> {
            80.dp
        }

        CardsSize.MEDIUM -> {
            120.dp
        }

        CardsSize.LARGE -> {
            180.dp
        }
    }
}


data class CardInfo(
    val cardNumber: String,
    val cardHolder: String,
    val providerDrawable: DrawableResource,
    val backgroundDrawable: DrawableResource
)