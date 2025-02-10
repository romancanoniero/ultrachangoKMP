package com.iyr.ultrachango.ui.screens.fidelization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iyr.ultrachango.data.models.CreditEntities
import com.iyr.ultrachango.utils.ui.device.getScreenWidth
import com.iyr.ultrachango.utils.ui.elements.buttonShapeSmall
import com.iyr.ultrachango.utils.ui.elements.customShapeBig
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback


@Composable
fun FidelizationSelectionDialog(
    creditEntities: List<CreditEntities>,
    onAcceptPressed: (List<CreditEntities>) -> Unit,
    onCancelPressed: () -> Unit
) {


    var selectedItems by remember { mutableStateOf(listOf<CreditEntities>()) }

    Column(
        modifier = Modifier.fillMaxSize()
                //    .background(Color.Green)
        ,
        verticalArrangement = Arrangement.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.70f),
            verticalArrangement = Arrangement.Center
        ) {
            Dialog(

                properties = DialogProperties(
                    usePlatformDefaultWidth = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true

                ),
                onDismissRequest = { /* Do nothing on outside click */ })
            {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.70f)
                                  .background(Color.White, shape = customShapeBig)
                            .border(1.dp, Color.Black, shape = customShapeBig)
                            .shadow(elevation = 4.dp, shape = customShapeBig)
                            .clip(customShapeBig),

                        verticalArrangement = Arrangement.Center
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                vertical = 20.dp,
                                horizontal = 10.dp
                            )
                        ) {

                            LazyVerticalGrid(
                                columns = if (getScreenWidth() > 600.dp) GridCells.Fixed(3) else GridCells.Fixed(
                                    2
                                ),
                                modifier = Modifier.weight(1f)
                                    .background(Color.Transparent)

                            ) {
                                items(creditEntities) { creditEntity ->

                                    CreditEntitiesCard(
                                        cardSize = CardsSize.SMALL,
                                        entity = creditEntity,
                                        selecteds = selectedItems,
                                        onClick = {
                                            triggerHapticFeedback()
                                            selectedItems =
                                                if (selectedItems.contains(creditEntity)) {
                                                    selectedItems - creditEntity
                                                } else {
                                                    selectedItems + creditEntity
                                                }
                                        })
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = buttonShapeSmall,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    ),
                                    onClick = {
                                        triggerHapticFeedback()
                                        onAcceptPressed(selectedItems) }
                                ) {
                                    Text("Aceptar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = buttonShapeSmall,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.LightGray,
                                        contentColor = Color.Black
                                    ),
                                    onClick = {
                                        triggerHapticFeedback()
                                        onCancelPressed}
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
            }
        }
    }
}


